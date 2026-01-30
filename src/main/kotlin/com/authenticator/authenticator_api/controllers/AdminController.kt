package com.authenticator.authenticator_api.controllers

import com.authenticator.authenticator_api.annotations.CurrentUser
import com.authenticator.authenticator_api.dto.BanDto
import com.authenticator.authenticator_api.dto.SetRoleDto
import com.authenticator.authenticator_api.dto.UserResponse
import com.authenticator.authenticator_api.enums.Role
import com.authenticator.authenticator_api.extensions.success
import com.authenticator.authenticator_api.models.User
import com.authenticator.authenticator_api.services.AdminService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.enums.EnumEntries

@RestController
@RequestMapping("/v1/admin")
class AdminController(
    private val adminService: AdminService
) {
    @GetMapping("/accounts")
    fun getAccounts(): ResponseEntity<List<UserResponse>> {
        val accounts = adminService.getAllAccounts()
        return ResponseEntity.ok(accounts)
    }

    @GetMapping("/accounts/pendants")
    fun getPendantAccounts(): ResponseEntity<List<UserResponse>> {
        val accounts = adminService.getPendingAccounts()
        return ResponseEntity.ok(accounts)
    }

    @PatchMapping("/accounts/approve/{accountId}")
    fun approveAccount(@PathVariable accountId: Long): ResponseEntity<Any> {
        adminService.approveAccount(accountId)
        return ResponseEntity.ok().success("Conta aprovada com sucesso! O usuário já pode fazer login.")
    }

    @GetMapping("/accounts/{login}")
    fun getAccount(@PathVariable login: String): ResponseEntity<UserResponse> {
        val account = adminService.getAccountByLogin(login)
        return ResponseEntity.ok(account)
    }

    @GetMapping("/roles")
    fun getRoles(): ResponseEntity<EnumEntries<Role>> {
        val roles = adminService.getAllRoles()
        return ResponseEntity.ok(roles)
    }

    @PatchMapping("/accounts/role")
    fun setRole(@RequestBody request: SetRoleDto, @CurrentUser user: User): ResponseEntity<Any> {
        adminService.updateRole(request, user)
        return ResponseEntity.ok().success("Cargo atualizado com sucesso!")
    }

    @PatchMapping("/accounts/ban")
    fun banAccount(@RequestBody request: BanDto, @CurrentUser user: User): ResponseEntity<Any> {
        adminService.banAccount(request, user)
        return ResponseEntity.ok().success("Conta bloqueada com sucesso.")
    }

    @PatchMapping("/accounts/unban/{accountId}")
    fun unbanAccount(@PathVariable accountId: Long): ResponseEntity<Any> {
        adminService.unbanAccount(accountId)
        return ResponseEntity.ok().success("Conta desbloqueada com sucesso.")
    }

    @GetMapping("/accounts/bans")
    fun getBans(): ResponseEntity<List<UserResponse>> {
        val accounts = adminService.getBannedAccounts()
        return ResponseEntity.ok(accounts)
    }

    @DeleteMapping("/accounts/{accountId}")
    fun delAccount(@PathVariable accountId: Long, @CurrentUser user: User): ResponseEntity<Any> {
        adminService.deleteAccount(accountId, user)
        return ResponseEntity.ok().success("Conta deletada com sucesso.")
    }
}