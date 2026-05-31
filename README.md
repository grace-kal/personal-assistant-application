# Guide Genie - AI-Powered Personal Assistant

> Master's thesis project - Faculty of Mathematics and Informatics, Plovdiv University "Paisii Hilendarski"  
> Specialisation: Software Technologies - Mobile Systems and Applications

Guide Genie is an Android personal assistant app that centralises daily life management in one place. It combines calendar scheduling, a news feed, an AI chatbot, and an AI-powered virtual wardrobe - all backed by a RESTful API.

## Features

**📅 Events & Calendar**  
Create and manage events with a title, start/end date and time, description, and location. Invite other users by email. View all events for a selected day in a calendar layout.

**👗 Smart Wardrobe**  
Upload photos of clothing items and let Microsoft Computer Vision automatically identify the garment type, colour, thickness, area, and season. Results are further refined by ChatGPT and saved to your personal wardrobe. Items can be browsed, edited, or deleted.

**📰 News Feed**  
A live feed of news articles pulled from an external news API, showing headline, image, description, publication date, and a link to the original source.

**🤖 AI Chatbot**  
A ChatGPT-powered assistant for in-app support - answers questions about how to use the app, navigates you to features, and keeps a history of previous conversations.

**👤 User Profiles**  
Registration and login with email/password. Profile includes first name, last name, country, and city (cascading dropdowns).

## Architecture

The app follows a client-server model:

- **Backend** - ASP.NET Core Web API (C#), structured in four layers: API (controllers), Models, DataAccess (repositories via EF Core), and Services (business logic)
- **Frontend** - Native Android app (Kotlin), using Activities, Fragments, RecyclerView adapters, and XML layouts. HTTP communication via OkHttpClient and Kotlin Coroutines for async operations
- **Communication** - HTTP requests with JSON payloads (GET, POST, PUT, DELETE)

## Data Storage

| Store | Purpose |
|---|---|
| Microsoft SQL Server (MSSQL) | Relational data - users, events, clothes, notes, chats, messages |
| Azure Blob Storage | Clothing images uploaded by users |

The database was built using Entity Framework Core with a Code-First approach.

## Tech Stack

| Layer | Technology |
|---|---|
| Backend language | C# / .NET |
| Backend framework | ASP.NET Core Web API |
| ORM | Entity Framework Core |
| Frontend language | Kotlin |
| Frontend platform | Android |
| Async | Kotlin Coroutines |
| Image loading | Glide |
| AI - image recognition | Microsoft Computer Vision API |
| AI - chatbot & data processing | OpenAI ChatGPT (GPT-4) |
| Cloud storage | Azure Blob Storage |
| News | External news API |
| Object mapping | AutoMapper |
| Design tool | Figma |
| DB design tool | dbdiagram.io |

## Project Structure

```
personal-assistant-application/
├── PersonalAssistant.Api/    # ASP.NET Core Web API (controllers, services, repositories, models)
└── PersonalAssistant.App/    # Android Kotlin app (activities, fragments, adapters, XML layouts)
```

## Getting Started

### Prerequisites

- .NET SDK (for the API)
- Android Studio (for the mobile app)
- Microsoft SQL Server instance
- Azure account (Blob Storage container for clothing images)
- OpenAI API key (for chatbot and clothing description processing)
- Microsoft Cognitive Services key (for Computer Vision)
- News API key (for the news feed)

### Running the Backend

1. Clone the repository.
2. Add your connection strings and API keys to the configuration (e.g. `appsettings.json`):
   - MSSQL connection string
   - Azure Blob Storage connection string
   - OpenAI API key
   - Microsoft Computer Vision endpoint and key
   - News API key
3. Apply database migrations:
   ```bash
   cd PersonalAssistant.Api
   dotnet ef database update
   ```
4. Run the API:
   ```bash
   dotnet run
   ```

### Running the Android App

1. Open `PersonalAssistant.App` in Android Studio.
2. Update the base URL in `ApiRequestHelper` to point to your running API.
3. Build and run on a device or emulator (API level 21+).

## Author

Gratsiya Kalinina - [LinkedIn](https://www.linkedin.com/in/gratsiya-kalinina/) · kalinina.grace@gmail.com
