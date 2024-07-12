package tia_sarwoedhi.belajar_spring_restful_api.controller;

import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import tia_sarwoedhi.belajar_spring_restful_api.entity.User;
import tia_sarwoedhi.belajar_spring_restful_api.model.ContactResponse;
import tia_sarwoedhi.belajar_spring_restful_api.model.PagingResponse;
import tia_sarwoedhi.belajar_spring_restful_api.model.WebResponse;
import tia_sarwoedhi.belajar_spring_restful_api.model.request.CreateContactRequest;
import tia_sarwoedhi.belajar_spring_restful_api.model.request.SearchContactRequest;
import tia_sarwoedhi.belajar_spring_restful_api.model.request.UpdateContactRequest;
import tia_sarwoedhi.belajar_spring_restful_api.service.ContactService;

import java.util.List;

import static org.hibernate.query.Page.page;

@RestController
public class ContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping(path = "/api/contacts", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<ContactResponse> create(User user, @RequestBody CreateContactRequest request) {
        ContactResponse contactResponse = contactService.create(user, request);
        return WebResponse.<ContactResponse>builder().data(contactResponse).build();
    }

    @GetMapping(path = "/api/contacts/{contactId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<ContactResponse> get(User user, @PathVariable("contactId") String id) {
        ContactResponse response = contactService.get(user, id);
        return WebResponse.<ContactResponse>builder().data(response).build();
    }

    @PutMapping(path = "/api/contacts/{contactId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<ContactResponse> update(User user, @PathVariable("contactId") String contactId, @RequestBody UpdateContactRequest request) {
        request.setId(contactId);
        ContactResponse contactResponse = contactService.update(user, request);
        return WebResponse.<ContactResponse>builder().data(contactResponse).build();
    }

    @DeleteMapping(path = "/api/contacts/{contactId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<String> delete(User user, @PathVariable("contactId") String id) {
        contactService.delete(user, id);
        return WebResponse.<String>builder().data("Ok").build();
    }

    @GetMapping(value = "/api/contacts",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<List<ContactResponse>> search(User user,
                                                     @RequestParam(value = "name", required = false) String name,
                                                     @RequestParam(value = "email", required = false) String email,
                                                     @RequestParam(value = "phone", required = false) String phone,
                                                     @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                     @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        SearchContactRequest request = SearchContactRequest.builder().
                page(page)
                .size(size)
                .email(email)
                .name(name)
                .phone(phone)
                .build();
        Page<ContactResponse> contactResponsePage = contactService.search(user, request);

        return WebResponse.<List<ContactResponse>>builder().data(contactResponsePage.getContent())
                .paging(PagingResponse.builder()
                        .currentPage(contactResponsePage.getNumber())
                        .size(contactResponsePage.getSize())
                        .totalPage(contactResponsePage.getTotalPages()).build()).build();

    }
}
