package com.geekbrains.septembermarket.paypal;

import com.geekbrains.septembermarket.entities.Order;
import com.geekbrains.septembermarket.services.OrderService;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping("/paypal")
public class PayPalController {
    private String clientId = "";
    private String clientSecret = "";
    private String mode = "sandbox";

    private APIContext apiContext = new APIContext(clientId, clientSecret, mode);

    private OrderService orderService;

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping("/buy")
    public String buy(@RequestParam(name = "orderId") Long orderId, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            Order order = orderService.findOneById(orderId);
            if (order.getStatus() == Order.Status.PAID) {
                model.addAttribute("message", "Ваш заказ оплачен. №" + order.getId());
                return "payresult";
            }
            if (order.getApprovalUrl() != null) {
                return "redirect:" + order.getApprovalUrl();
            }
            Payer payer = new Payer();
            payer.setPaymentMethod("paypal");
            RedirectUrls redirectUrls = new RedirectUrls();
            redirectUrls.setCancelUrl("http://localhost:8189/market/paypal/cancel");
            redirectUrls.setReturnUrl("http://localhost:8189/market/paypal/success");

            Amount amount = new Amount();
            amount.setCurrency("RUB");
            amount.setTotal(String.valueOf(order.getPrice()));

            Transaction transaction = new Transaction();
            transaction.setAmount(amount);
            transaction.setDescription("Оплата в September Market заказа №" + order.getId());

            List<Transaction> transactions = new ArrayList<>();
            transactions.add(transaction);

            Payment payment = new Payment();
            payment.setPayer(payer);
            payment.setRedirectUrls(redirectUrls);
            payment.setTransactions(transactions);
            payment.setIntent("sale");

            Payment doPayment = payment.create(apiContext);

            //TODO Добавить поле PaymentID в таблицу Orders. Искать заказ по этому полю.
//            System.out.println(order.getId());
//            System.out.println(doPayment.getId());


            Iterator<Links> links = doPayment.getLinks().iterator();

            while (links.hasNext()) {
                Links link = links.next();
//                System.out.println(link.getHref());
//                System.out.println(link.getRel());
                if (link.getRel().equalsIgnoreCase("approval_url")) {
                    order.setPaymentId(doPayment.getId());
                    order.setApprovalUrl(link.getHref());
                    orderService.save(order);
                    return "redirect:" + link.getHref();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("message", "Вы сюда не должны были попасть...");
        return "payresult";
    }

    @GetMapping("/success")
    public String success(HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            String paymentId = request.getParameter("paymentId");
            String payerId = request.getParameter("PayerID");

            if (paymentId == null || paymentId.isEmpty() || payerId == null || payerId.isEmpty()) {
                return "redirect:/";
            }

            Payment payment = new Payment();
            payment.setId(paymentId);

            PaymentExecution paymentExecution = new PaymentExecution();
            paymentExecution.setPayerId(payerId);

            Payment executedPayment = payment.execute(apiContext, paymentExecution);

            if (executedPayment.getState().equals("approved")) {
                //TODO Добавить поле PaymentID в таблицу Orders. Искать заказ по этому полю.
                Order order = orderService.findOneByPaymentId(paymentId);
//                System.out.println(paymentId);
                order.setStatus(Order.Status.PAID);
                orderService.save(order);
                model.addAttribute("message", "Ваш заказ оплачен. №" + order.getId());
            } else {
                model.addAttribute("message", "Что-то пошло не так при оплате заказа, попробуйте повторить операцию");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "payresult";
    }

    @GetMapping("/cancel")
    public String cancel(Model model) {
        model.addAttribute("message", "Оплата заказа не была проведена. Возможно Вы отменили ее...");
        return "payresult";
    }
}
