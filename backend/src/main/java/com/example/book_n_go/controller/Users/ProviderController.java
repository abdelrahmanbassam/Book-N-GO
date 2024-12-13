package com.example.book_n_go.controller.Users;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/provider")
@PreAuthorize("hasAnyRole('Role.ADMIN.name()', 'Role.PROVIDER.name()')")
public class ProviderController {

    @GetMapping
    @PreAuthorize("hasAnyAuthority('provider:read', 'admin:read')")
    public String get() {
        return "Provider: Get provider controller";
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('provider:write', 'admin:write')")
    public String post() {
        return "Provider: Post provider controller";
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('provider:update', 'admin:update')")
    public String put() {
        return "Provider: Put provider controller";
    }

    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('provider:delete', 'admin:delete')")
    public String delete() {
        return "Provider: Delete provider controller";
    }

}