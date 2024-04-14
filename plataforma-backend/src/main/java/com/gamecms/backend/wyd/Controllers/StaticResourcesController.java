package com.gamecms.backend.wyd.Controllers;

import com.gamecms.backend.wyd.Models.Noticia;
import com.gamecms.backend.wyd.Models.ServerCustomer;
import com.gamecms.backend.wyd.Services.NoticiasServiceImpl;
import com.gamecms.backend.wyd.Services.ServerCustomerServiceImpl;
import com.google.gson.Gson;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/staticresources")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "StaticResources Controller", description = "Endpoints for Serving Static Resources like bypass, launcher and etc.")
public class StaticResourcesController {

    private final NoticiasServiceImpl noticiasService;
    private final ServerCustomerServiceImpl serverService;
    private final Gson gson;

    @GetMapping(path ="/customer/{customerId}/wydlauncher")
    public String getIndex(@PathVariable Long customerId, Model model){
        List<Noticia> noticias = noticiasService.findFirst10Noticias(customerId);
        ServerCustomer customer = serverService.findById(customerId);
        model.addAttribute("noticias", noticias);
        model.addAttribute("servidor", customer.getServerFantasyName());
        model.addAttribute("image", customer.getLauncherImage());
        log.info("Jogador - Servindo dados de launcher -  customer: " + customerId);
        return "wydlauncher";
    }

    // TODO: Recurso repetido rever (publicconfigurationsforfrontendcontroller)
    @GetMapping(path ="/customer/{customerId}/configuracoes", produces = "application/json")
    public String getSiteParams(@PathVariable Long customerId){
        ServerCustomer customer = serverService.findById(customerId);
        customer.setPassword(null);
        customer.setEmail(null);
        customer.setSshKey(null);
        customer.setDatabaseName(null);
        customer.setId(null);
        customer.setUser(null);
        customer.setServerIpv4Address(null);
        customer.setServerIpv6Address(null);
        return gson.toJson(customer);
    }

}
