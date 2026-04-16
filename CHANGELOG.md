# Changelog

All notable changes to this project will be documented in this file.

## [1.0.0] - Initial Release

### Added

- Copy path relative to multiple parent folders
- Project tree decorator for relative parents
- Settings panel to manage parent folders
- Context menu actions:
    * Set as Relative Parent
    * Copy Multi Relative Path
- Popup selector when multiple relative paths exist

## [1.0.1] - Apply some fix - 2026-04-16

### Changed

- Replaced deprecated project root lookup with `Project.basePath`-based resolution for better compatibility
- Improved the settings folder picker behavior for selecting relative parent folders inside the current project
- Internal cleanup around project view decoration compatibility
