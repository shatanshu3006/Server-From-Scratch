#  Simple Java HTTP Server

A custom-built, lightweight HTTP server written from scratch in Java to explore sockets, threads, HTTP request parsing, and static file serving — **no frameworks used**.
RFC based standard features aligning with the latest norms and practices for building HTTP Servers.

---

## ⚙ Features

- ✔️ Custom HTTP parser (scannerless)
- ✔️ Multi-threaded socket handling
- ✔️ Static file serving from `WebRoot/`
- ✔️ MIME type detection
- ✔️ JSON-based server configuration
- ✔️ Custom HTTP error handling
- ✔️ JUnit 5 test coverage for parsing, MIME, and routing

---

##  Request Lifecycle
<img width="2792" height="1392" alt="image" src="https://github.com/user-attachments/assets/94567281-c0e0-403a-ab59-3186d9a81be3" />

 
---

##  Project File Structure

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




