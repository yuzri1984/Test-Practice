package test.practice.Test.Practice.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import test.practice.Test.Practice.models.Currency;
import test.practice.Test.Practice.services.CurrencyServices;

@Controller
@RequestMapping("/currency")
public class CurrencyController {
    
    @Autowired
    private CurrencyServices currSvc;

    @GetMapping
    public String getCurrency(Model model, @RequestParam String ctype){
        List<Currency> currency = currSvc.getCurrency(ctype);
        model.addAttribute("currency",currency);
        return "currency";

    }

}
