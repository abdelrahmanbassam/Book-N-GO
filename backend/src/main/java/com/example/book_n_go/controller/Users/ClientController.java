package com.example.book_n_go.controller.Users;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/client")
@PreAuthorize("hasAnyRole('Role.ADMIN.name()', 'Role.CLIENT.name()')")
public class ClientController {

    @GetMapping()
    @PreAuthorize("hasAnyAuthority('client:read', 'admin:read')")
    public String get() {
        return "Client: Get client controller";
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('client:write', 'admin:write')")
    public String post() {
        return "Client: Post client controller";
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('client:update', 'admin:update')")
    public String put() {
        return "Client: Put client controller";
    }

    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('client:delete', 'admin:delete')")
    public String delete() {
        return "Client: Delete client controller";
    }

}