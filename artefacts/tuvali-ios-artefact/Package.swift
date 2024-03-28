// swift-tools-version: 5.7.1
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

let package = Package(
    name: "test-folder",
    products: [
        // Products define the executables and libraries a package produces, making them visible to other packages.
        .library(
            name: "test-folder",
            targets: ["test-folder"]),
    ],
    dependencies: [
        .package(url: "https://github.com/1024jp/GzipSwift", from: "6.0.0"),
	.package(url: "https://github.com/ivanesik/CrcSwift.git", from: "0.0.3")
    ],
    targets: [
        // Targets are the basic building blocks of a package, defining a module or a test suite.
        // Targets can depend on other targets in this package and products from dependencies.
        .target(
            name: "test-folder"),
        .testTarget(
            name: "test-folderTests",
            dependencies: ["test-folder"]),
    ]
)
