package com.springdata.demo.controllers;


import com.springdata.demo.models.Client;
import com.springdata.demo.models.ClientDto;
import com.springdata.demo.repositories.ClientRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@Controller
@RequestMapping("/clients")
public class ClientsController {
    @Autowired
    private ClientRepository clientRepository;

    @GetMapping({"","/"})
    public String getClients(Model model) {
        var clients = clientRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("clients",clients);
        return "clients/index";
    }

    @GetMapping("/create")
    public String createClient(Model model) {
        ClientDto clientDto = new ClientDto();
        model.addAttribute("clientDto",clientDto);
        return "clients/create";
    }

    @PostMapping("/create")
    public String createClient(
         @Valid @ModelAttribute ClientDto clientDto,
         BindingResult result
    ){
        if(clientRepository.findByEmail(clientDto.getEmail()) != null){
            result.addError(
                    new FieldError("clientDto","email", clientDto.getEmail()
                    , false, null, null, "Email address is already used")
            );
        }
        if (result.hasErrors()) {
            return "clients/create";
        }
        Client client = new Client();
        client.setFirstName(clientDto.getFirstName());
        client.setLastName(clientDto.getLastName());
        client.setEmail(clientDto.getEmail());
        client.setPhone(clientDto.getPhone());
        client.setAddress(clientDto.getAddress());
        client.setStatus(clientDto.getStatus());
        client.setCreatedAt(new Date());

        clientRepository.save(client);

            return "redirect:/clients";
    }
}