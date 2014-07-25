/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lyncode.dataencoder.controller;

import com.lyncode.dataencoder.crypt.CryptUtils;
import com.lyncode.dataencoder.persistence.api.PublicKeyRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {
    private static Logger log = Logger.getLogger(IndexController.class);

    @Autowired
    private PublicKeyRepository repository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String indexAction (ModelMap modelMap) {
        modelMap.addAttribute("keys", repository.findAll());
        return "index";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String indexAction (ModelMap modelMap,
                               @RequestParam("text") String toEncrypt,
                               @RequestParam("name") String key) {
        try {
            modelMap.addAttribute("encryption", repository.get(key).encrypt(toEncrypt));
            modelMap.addAttribute("keyValue", key);
            modelMap.addAttribute("text", toEncrypt);
        } catch (CryptUtils.EncryptException e) {
            modelMap.addAttribute("error", e.getMessage());
            log.error(e.getMessage(), e);
        }
        return indexAction(modelMap);
    }

    @RequestMapping("/login")
    public String loginAction () {
        return "login";
    }
}
