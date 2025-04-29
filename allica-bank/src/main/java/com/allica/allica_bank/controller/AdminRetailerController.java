package com.allica.allica_bank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.allica.allica_bank.model.retailer.RetailerRequestDTO;
import com.allica.allica_bank.model.retailer.RetailerResponseDTO;
import com.allica.allica_bank.service.AdminRetailerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin/retailer")
public class AdminRetailerController {
	
	@Autowired
	private AdminRetailerService adminRetailerService;

    @PostMapping
    public RetailerResponseDTO createRetailer(@RequestBody @Valid RetailerRequestDTO retailerRequestDTO) {
        return adminRetailerService.createRetailer(retailerRequestDTO);
    }

    @GetMapping("/{id}")
    public RetailerResponseDTO getRetailer(@PathVariable Long id) {
        return adminRetailerService.getRetailerById(id);
    }

    @GetMapping
    public List<RetailerResponseDTO> getAllRetailers() {
        return adminRetailerService.getAllRetailers();
    }

    @PutMapping("/{id}")
    public RetailerResponseDTO updateRetailer(@PathVariable Long id, @RequestBody @Valid RetailerRequestDTO retailerRequestDTO) {
        return adminRetailerService.updateRetailer(id, retailerRequestDTO);
    }

    @DeleteMapping("/{id}")
    public String deleteRetailer(@PathVariable Long id) {
        adminRetailerService.deleteRetailer(id);
        return "Retailer deleted successfully";
    }
}
