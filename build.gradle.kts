import io.izzel.taboolib.gradle.*
import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_1_8
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    java
    id("io.izzel.taboolib") version "2.0.27"
    id("org.jetbrains.kotlin.jvm") version "2.2.0"
    `maven-publish`
}

taboolib {
    env {
        install(Bukkit)
        install(BukkitUtil)
        install(Basic)
        install(I18n)
        install(Metrics)
        install(MinecraftChat)
    }
    description {
        name = "OneBot"
        desc("链接OneBot的协议的一个Minecraft插件")
        contributors {
            name("BingZi-233")
        }
    }
    version { taboolib = "6.2.3-ee81cb0" }
    relocate("org.java_websocket", "online.bingzi.onebot.rely.java_websocket")
    relocate("com.google.gson", "online.bingzi.onebot.rely.gson")
    relocate("okhttp3", "online.bingzi.onebot.rely.okhttp3")
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("ink.ptms.core:v12004:12004:mapped")
    compileOnly("ink.ptms.core:v12004:12004:universal")
    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("libs"))

    // WebSocket 客户端
    taboo("org.java-websocket:Java-WebSocket:1.5.4")
    // JSON 处理
    taboo("com.google.code.gson:gson:2.10.1")
    // HTTP 客户端（可选，用于API调用）
    taboo("com.squareup.okhttp3:okhttp:4.12.0")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        jvmTarget.set(JVM_1_8)
        freeCompilerArgs.add("-Xjvm-default=all")
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "online.bingzi"
            artifactId = "onebot"
            // 使用 gradle.properties 中的版本号
            version = project.version.toString()
            
            // 直接发布API JAR文件
            val apiJarFile = file("build/libs/${rootProject.name}-${rootProject.version}-api.jar")
            artifact(apiJarFile)
            
            pom {
                name.set("OneBot")
                description.set("链接OneBot协议的Minecraft插件")
                url.set("https://github.com/BingZi-233/OneBot")
                
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                
                developers {
                    developer {
                        id.set("BingZi-233")
                        name.set("BingZi-233")
                    }
                }
            }
        }
    }
    
    repositories {
        maven {
            name = "AeolianCloud"
            url = uri("https://repo.aeoliancloud.com/repository/releases/")
            credentials {
                username = System.getenv("MAVEN_USERNAME") ?: project.findProperty("mavenUsername") as String?
                password = System.getenv("MAVEN_PASSWORD") ?: project.findProperty("mavenPassword") as String?
            }
            authentication {
                create<BasicAuthentication>("basic")
            }
            // 添加上传方法配置
            isAllowInsecureProtocol = false
        }
    }
}

// 确保发布任务依赖于API构建任务
tasks.withType<PublishToMavenRepository> {
    dependsOn("taboolibBuildApi")
}

