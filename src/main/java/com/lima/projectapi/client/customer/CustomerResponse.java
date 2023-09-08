package com.lima.projectapi.client.customer;


public record CustomerResponse(Integer id,
                               String nome,
                               String email,
                               boolean ativo) {
}
