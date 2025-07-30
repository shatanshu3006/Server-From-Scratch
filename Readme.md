#  Pingy : Simple Java HTTP Server

A custom-built, lightweight HTTP server written from scratch in Java to explore sockets, threads, HTTP request parsing, and static file serving — **no frameworks used**.
RFC based standard features aligning with the latest norms and practices for building HTTP Servers.

<img width="944" height="734" alt="image" src="https://github.com/user-attachments/assets/6bac722f-f62b-44a7-9232-92822919fc30" />


---

## ⚙ Features

- Custom HTTP parser (scannerless)
- Multi-threaded socket handling
- Static file serving from `WebRoot/`
- MIME type detection
- JSON-based server configuration
- Custom HTTP error handling
- JUnit 5 test coverage for parsing, MIME, and routing

---

##  Request Lifecycle
<img width="2792" height="1392" alt="image" src="https://github.com/user-attachments/assets/94567281-c0e0-403a-ab59-3186d9a81be3" />

 
---

##  Project File Structure

<img width="5352" height="1164" alt="image" src="https://github.com/user-attachments/assets/5efc4fb3-1e0b-4a1e-97cc-bd57e412193c" />


<details>
<summary>📁 Click to expand</summary>

📦 Server From Scratch  
├── 📁 WebRoot                         # Static assets  
│   ├── favicon.ico  
│   ├── index.html  
│   └── logo.png  
├── 📁 src  
│   ├── 📁 main  
│   │   └── 📁 java  
│   │       └── 📁 com  
│   │           └── 📁 coderfromscratch  
│   │               ├── 📁 http                  # Core HTTP components  
│   │               │   ├── BadHttpVersionException.java  
│   │               │   ├── HttpMethod.java  
│   │               │   ├── HttpParser.java  
│   │               │   ├── HttpParsingException.java  
│   │               │   ├── HttpRequest.java  
│   │               │   ├── HttpStatusCode.java  
│   │               │   └── HttpVersion.java  
│   │               ├── 📁 httpserver  
│   │               │   ├── 📁 config            # JSON config loader  
│   │               │   │   ├── Configuration.java  
│   │               │   │   ├── ConfigurationManager.java  
│   │               │   │   └── HttpConfigurationException.java  
│   │               │   ├── 📁 core              # Main server engine  
│   │               │   │   ├── HttpConnectionWorkerThread.java  
│   │               │   │   ├── ServerListenerThread.java  
│   │               │   │   └── 📁 io  
│   │               │   │       ├── ReadFileException.java  
│   │               │   │       ├── WebRootHandler.java  
│   │               │   │       └── WebRootNotFoundException.java  
│   │               └── 📁 util  
│   │                   ├── HttpServer.java      # Main entry point  
│   │                   └── Json.java            # JSON helper  
│   └── 📁 test  
│       └── 📁 java  
│           └── 📁 com  
│               └── 📁 coderfromscratch  
│                   ├── HttpParserTest.java  
│                   ├── HttpHeadersParserTest.java  
│                   ├── HttpVersionTest.java  
│                   └── 📁 httpserver  
│                       └── 📁 core  
│                           └── 📁 io  
│                               └── WebRootHandlerTest.java  
├── 📄 http.json                      # Configuration file  
├── 📄 pom.xml                        # Maven build file  
└── 📄 README.md                      # Project overview  
</details>

---




