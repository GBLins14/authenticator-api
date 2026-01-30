package com.authenticator.authenticator_api.services

import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.client.RestClient

@Service
class ForgotPasswordService(
    @Value($$"${resend.resend-api-key}") private val apiKey: String,
    @Value($$"${app.password-recovery.token-expiration-minutes}") private val TOKEN_EXPIRATION_MINUTES: Long,
) {
    private val logger = LoggerFactory.getLogger(ForgotPasswordService::class.java)

    private val restClient = RestClient.builder()
        .baseUrl("https://api.resend.com")
        .defaultHeader("Authorization", "Bearer $apiKey")
        .build()

    @Async
    fun send(email: String, username: String, link: String) {
        val htmlContent = """
        <!DOCTYPE html>
        <html lang="pt-BR">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Redefinição de Senha</title>
            </head>
        <body style="margin: 0; padding: 0; background-color: #f6f9fc; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; -webkit-font-smoothing: antialiased;">
            
            <div style="display: none; max-height: 0px; overflow: hidden;">
                Recebemos um pedido para alterar sua senha. Clique para prosseguir.
            </div>
    
            <table role="presentation" border="0" cellpadding="0" cellspacing="0" width="100%" style="background-color: #f6f9fc;">
                <tr>
                    <td align="center" style="padding: 40px 10px;">
                        <table role="presentation" border="0" cellpadding="0" cellspacing="0" width="100%" style="max-width: 600px; background-color: #ffffff; border-radius: 12px; box-shadow: 0 4px 15px rgba(0,0,0,0.05); overflow: hidden; border: 1px solid #e1e4e8; margin: 0 auto;">
                            
                            <tr>
                                <td align="center" style="background-color: #ffffff; padding: 40px 0 20px 0; border-bottom: 1px solid #f0f0f0;">
                                    
                                    <div style="background-color: #C084FC; width: 50px; height: 50px; border-radius: 12px; margin: 0 auto 10px auto;">
                                        <table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
                                            <tr>
                                                <td align="center" valign="middle" style="height: 50px;">
                                                    <span style="font-size: 26px; line-height: 1; display: block;">🔐</span>
                                                </td>
                                            </tr>
                                        </table>
                                    </div>
                            
                                    <h2 style="margin: 10px 0 0 0; color: #1a1a1a; font-size: 22px; letter-spacing: -0.5px;">LOBBY APP</h2>
                                </td>
                            </tr>
    
                            <tr>
                                <td align="center" style="padding: 40px 30px; text-align: center;">
                                    <h1 style="color: #1a202c; font-size: 24px; margin: 0 0 20px 0;">Recuperação de Senha</h1>
                                    
                                    <p style="color: #4a5568; font-size: 16px; line-height: 1.6; margin-bottom: 25px;">
                                        Olá, <strong>$username</strong>.<br>
                                        Recebemos uma solicitação para redefinir a senha da sua conta. Se foi você, clique no botão abaixo para criar uma nova senha:
                                    </p>
    
                                    <table role="presentation" border="0" cellpadding="0" cellspacing="0" width="100%" style="margin: 30px 0;">
                                        <tr>
                                            <td align="center">
                                                <a href="$link" target="_blank" style="background-color: #C084FC; color: #ffffff; font-size: 16px; font-weight: 600; padding: 14px 32px; border-radius: 8px; text-decoration: none; display: inline-block; box-shadow: 0 4px 6px rgba(79, 70, 229, 0.2);">
                                                    Redefinir Minha Senha
                                                </a>
                                            </td>
                                        </tr>
                                    </table>
    
                                    <p style="color: #718096; font-size: 14px; margin-bottom: 30px;">
                                        ⏰ Este link expira em <strong>$TOKEN_EXPIRATION_MINUTES minutos</strong> por segurança.
                                    </p>
    
                                    <div style="background-color: #fff5f5; border-left: 4px solid #c53030; padding: 15px; text-align: left; margin-bottom: 30px; border-radius: 4px; display: inline-block; width: 90%;">
                                        <p style="color: #c53030; font-size: 14px; margin: 0; font-weight: 600; text-align: left;">Não pediu isso?</p>
                                        <p style="color: #742a2a; font-size: 13px; margin: 5px 0 0 0; text-align: left;">
                                            Pode ignorar este e-mail com segurança. Ninguém consegue acessar sua conta sem a senha atual.
                                        </p>
                                    </div>
    
                                    <div style="border-top: 1px solid #edf2f7; padding-top: 20px; text-align: center;">
                                        <p style="color: #a0aec0; font-size: 12px; margin-bottom: 10px;">Problemas com o botão? Copie o link abaixo:</p>
                                        <p style="word-break: break-all; margin: 0;">
                                            <a href="$link" style="color: #C084FC; font-size: 12px; text-decoration: none;">$link</a>
                                        </p>
                                    </div>
                                </td>
                            </tr>
    
                            <tr>
                                <td align="center" style="background-color: #f8fafc; padding: 20px; text-align: center; border-top: 1px solid #f0f0f0;">
                                    <p style="color: #a0aec0; font-size: 12px; margin: 0;">
                                        © 2025 Lobby App - Segurança e Controle<br>
                                        Enviado automaticamente pelo sistema.
                                    </p>
                                </td>
                            </tr>
                        </table>
                        
                        </td>
                </tr>
            </table>
        </body>
        </html>
    """.trimIndent()

        val emailRequest = mapOf(
            "from" to "Lobby <nao-responda@mail.uselobby.com.br>",
            "to" to listOf(email),
            "subject" to "Lobby: Recuperação de Senha",
            "html" to htmlContent
        )

        try {
            val response = restClient.post()
                .uri("/emails")
                .contentType(MediaType.APPLICATION_JSON)
                .body(emailRequest)
                .retrieve()
                .toBodilessEntity()
        } catch (e: Exception) {
            logger.error("❌ Falha crítica ao enviar email via API: ${e.message}", e)
        }
    }
}