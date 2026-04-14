# Multi Relative Path

<!-- Plugin description -->
Multi Relative Path is a plugin for JetBrains IDEs that allows copying file paths relative to **multiple configurable parent directories**.

This is useful for projects that contain multiple modules, packages, or layered structures where a single relative root is not enough.
<!-- Plugin description end -->

## ✨ Features

* Copy file paths relative to **multiple parent folders**
* Mark folders as **Relative Parent** directly from the Project view
* Smart **context menu integration**
* Clean **IDE-style popup chooser** when multiple relative paths exist
* Visual **decorator** in the Project tree
* Configuration panel in **Settings → Multi Relative Path**

## 📦 Example

Project structure:

```
project
 ├─ module-a
 │   └─ src/main/java/App.java
 └─ module-b
     └─ src/main/java/App.java
```

Configured relative parents:

```
module-a
module-b
```

Copying a file path could produce:

```
src/main/java/App.java
```

instead of the full project path.

## ⚙️ Configuration

Open:

```
Settings → Tools → Multi Relative Path
```

From there you can:

* Add relative parent folders
* Remove existing ones
* Browse the project structure

## 🖱 Usage

Right-click a file in the Project view:

```
Multi Relative Path → Copy Multi Relative Path
```

If multiple relative paths exist, a chooser popup will appear.

## 🧩 Compatible IDEs

All JetBrains IDEs supporting the IntelliJ Platform:

* IntelliJ IDEA
* WebStorm
* PyCharm
* Rider
* GoLand
* CLion
* PhpStorm

## 🛠 Development

Built using the official:

JetBrains IntelliJ Platform Plugin Template

Run the plugin locally:

```
./gradlew runIde
```

## 📜 License

MIT License
