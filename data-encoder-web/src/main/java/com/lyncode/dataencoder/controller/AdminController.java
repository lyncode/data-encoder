package com.lyncode.dataencoder.controller;

import com.lyncode.dataencoder.persistence.api.PublicKeyRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

@Controller
public class AdminController {    private static Logger log = Logger.getLogger(IndexController.class);

    @Autowired
    private PublicKeyRepository repository;

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String indexAction (ModelMap model) {
        model.addAttribute("keys", repository.findAll());
        return "admin";
    }

    @RequestMapping(value = "/admin", method = RequestMethod.POST)
    public String indexAction (ModelMap model, @RequestParam("name") String keyName) {
        try {
            model.addAttribute("newKey", repository.create(keyName));
            model.addAttribute("newKeyPub", repository.get(keyName).pub());
            model.addAttribute("keyValue", keyName);
        } catch (PublicKeyRepository.KeyPairCreationException e) {
            model.addAttribute("error", e.getMessage());
            log.error(e.getMessage(), e);
        }
        model.addAttribute("keys", repository.findAll());
        return "admin";
    }

    @RequestMapping(value = "/admin/{keyName}/download", method = RequestMethod.GET)
    public String downloadAction (ModelMap model, @PathVariable("keyName") String keyName) {
        model.addAttribute("key", repository.get(keyName));
        return "download";
    }
    @RequestMapping(value = "/admin/{keyName}/download", method = RequestMethod.POST)
    public String downloadAction (ModelMap model,
                                  @PathVariable("keyName") String keyName,
                                  @RequestParam("password") String password,
                                  @RequestParam("alias") String alias,
                                  HttpServletResponse response) {
        try {
            response.setContentType("application/x-download");
            response.setHeader( "Content-Disposition", "filename=keystore.jks" );
            repository.get(keyName).writeKeyStore(alias, password, response.getOutputStream());
            response.getOutputStream().flush();
            return null;
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            log.error(e.getMessage(), e);
            return "download";
        }
    }

}
