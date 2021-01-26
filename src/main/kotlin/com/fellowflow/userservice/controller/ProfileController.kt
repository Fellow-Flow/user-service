package com.fellowflow.userservice.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author Nicholas Dietz @ Fellow-Flow
 **/
@RestController
@RequestMapping("v1/profile")
class ProfileController {

    @GetMapping()
    fun test(): ResponseEntity<String> {
        return ResponseEntity.ok("test passed!")
    }
}
