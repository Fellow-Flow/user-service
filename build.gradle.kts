import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.4.2"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("org.jlleitschuh.gradle.ktlint") version "9.4.1"
	kotlin("jvm") version "1.4.21"
	kotlin("plugin.spring") version "1.4.21"
}

group = "com.fellowflow"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
	google()
	jcenter()
	maven {
		name = "github"
		url = uri("https://maven.pkg.github.com/nicos-dev/spring-keycloak-starter")
		credentials {
			username = (project.properties["githubUser"] ?: System.getenv("GITHUB_USER")).toString()
			password = (project.properties["githubPassword"] ?: System.getenv("GITHUB_TOKEN")).toString()
		}
	}
}

dependencies {
	implementation("org.projectlombok:lombok:1.18.12")
	implementation("org.springframework.boot:spring-boot-starter-actuator:2.3.3.RELEASE")
	implementation("io.springfox:springfox-boot-starter:3.0.0")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.2")
	implementation("org.springframework.boot:spring-boot-starter-security:2.3.3.RELEASE")
	implementation("org.keycloak:keycloak-spring-boot-starter:12.0.1")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("com.nicos-dev:spring-keycloak-starter:0.0.1-SNAPSHOT")
	compileOnly("org.projectlombok:lombok")
	runtimeOnly("com.h2database:h2")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

dependencyManagement {
	imports {
		mavenBom("org.keycloak.bom:keycloak-adapter-bom:12.0.1")
	}
}

apply {
	plugin("com.jaredsburrows.license")
	plugin("org.jlleitschuh.gradle.ktlint")
}

buildscript {
	repositories {
		mavenCentral()
		jcenter()
		google()
	}

	dependencies {
		classpath("com.jaredsburrows:gradle-license-plugin:0.8.80")
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
	debug.set(true)
	ignoreFailures.set(true)
	filter {
		exclude("*.kts")
	}
}