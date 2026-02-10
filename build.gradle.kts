plugins {
	kotlin("jvm") version "2.2.21"
	kotlin("plugin.spring") version "2.2.21"
	id("org.springframework.boot") version "4.0.0"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.jetbrains.kotlin.plugin.jpa") version "1.9.25"
}

group = "com.authenticator"
version = "1.0.0"
description = "Authenticator"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("tools.jackson.module:jackson-module-kotlin")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-security-test")
	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
	implementation("com.giffing.bucket4j.spring.boot.starter:bucket4j-spring-boot-starter:0.1.5")
	implementation("org.springframework.boot:spring-boot-starter-cache")
	implementation("javax.cache:cache-api")
	implementation("com.github.ben-manes.caffeine:caffeine:3.1.8")
	implementation("jakarta.servlet:jakarta.servlet-api:6.0.0")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.postgresql:postgresql")
	implementation("io.github.cdimascio:java-dotenv:5.2.2")
	implementation("me.paulschwarz:spring-dotenv:4.0.0")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
	implementation("com.bucket4j:bucket4j-core:8.9.0")
	implementation("com.resend:resend-java:2.2.0")
	implementation("com.stripe:stripe-java:24.12.0")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.0")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
	}
}