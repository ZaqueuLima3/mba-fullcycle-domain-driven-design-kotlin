import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.5"
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
    kotlin("plugin.jpa") version "1.9.24"
    kotlin("plugin.serialization") version "2.0.0-RC3"
}

group = "dev.zaqueu.domain-driven-design-kotlin"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    // spring
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.0-RC")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0-RC")

    // jackson
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")

    // mysql
    runtimeOnly("com.mysql:mysql-connector-j")

    // tests ----------------

    // database
    testRuntimeOnly("com.h2database:h2")

    // spring
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // junit
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // test container
    testImplementation("org.testcontainers:testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:mysql")

    // mockk
    testImplementation("io.mockk:mockk:1.13.11")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

//testing {
//    suites {
//        val test by getting(JvmTestSuite::class) {
//            useJUnitJupiter()
//            testType.set(TestSuiteType.UNIT_TEST)
//        }
//
//        val integrationTest by registering(JvmTestSuite::class) {
//            useJUnitJupiter()
//            testType.set(TestSuiteType.INTEGRATION_TEST)
//
//            sources {
//                kotlin {
//                    srcDir("src/integrationTest/kotlin")
//                }
//                resources {
//                    setSrcDirs(
//                        listOf(
//                            "src/main/resources",
//                            "src/test/resources",
//                            "src/integrationTest/resources"
//                        )
//                    )
//                }
//            }
//
//            targets {
//                all {
//                    testTask.configure {
//                        shouldRunAfter(test)
//                    }
//                }
//            }
//        }
//    }
//}

tasks.withType<Test> {
    useJUnitPlatform()
}
