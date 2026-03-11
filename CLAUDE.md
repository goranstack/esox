# Esox - Java Swing Binding Library

## Overview
Esox binds model values to Java Swing components using the observable pattern and Java reflection. It synchronizes value changes bidirectionally between models and Swing components. Developed by Dennis Malmström, maintained by Göran Stäck.

## Project Structure
Multi-module Maven project (`com.github.goranstack.esox`, version 2.1.2-SNAPSHOT):

- **esox-core** (`nu.esox.util`): Observable model classes (`Observable`, `ObservableIF`, `ObservableListener`, `ObservableEvent`, `ObservableList`, etc.)
- **esox-gui** (`nu.esox.gui`, `nu.esox.gui.aspect`): Swing binding classes — `ModelPanel`, `ModelFrame`, `ModelDialog`, `AbstractAdapter`, and concrete adapters (`TextFieldAdapter`, `ComboBoxAdapter`, `SpinnerAdapter`, `LabelTextAdapter`, `SubModelAdapter`, etc.)
- **esox-xml**: XML utilities
- **esox-account**: Example application using the library
- **fish**: Example application using the library
- **gh-pages**: AsciiDoc documentation site (published to goranstack.github.io/esox)

## Key Concepts
- **Model**: Domain entity extending `nu.esox.util.Observable`, fires `fireValueChanged(aspectName, value)` on changes
- **Aspect**: A named property of a model with get/set methods
- **Model Owner**: Holds a reference to a model; implements `ModelOwnerIF` (e.g., `ModelPanel`)
- **Aspect Adapter**: Connects an aspect to a Swing component (aspect projector), keeping them synchronized via reflection-based method names
- **Submodel Adapter**: Synchronizes a submodel's model owner when the parent model changes

## Build
- Java 8 (source/target 1.8)
- Build: `mvn clean install`
- Release profile: `mvn -P release` (signs artifacts, deploys to Maven Central via Sonatype Central Portal)
- License: Apache 2.0

## CI/CD — GitHub Actions (migrated from Travis CI)

### Build workflow (`.github/workflows/build.yml`)
- Triggers on push/PR to `master`
- Builds with JDK 8 (Temurin) + Graphviz
- Deploys `gh-pages/target/generated-docs` to GitHub Pages on master push

### Release workflow (`.github/workflows/release.yml`)
- Triggers on tag push
- Sets POM version from tag, deploys to Maven Central with `-P release`
- `setup-java` handles GPG import and Maven `settings.xml`

### Setup required for releases
1. **GitHub Pages**: Settings > Pages > Source → select "GitHub Actions"
2. **Sonatype Central Portal**: Log in at https://central.sonatype.com, migrate OSSRH namespace, generate a user token
3. **Repository secrets** (Settings > Secrets and variables > Actions > New repository secret — use repository secrets, not environment secrets):
   - `CENTRAL_TOKEN_USERNAME` — Central Portal token username
   - `CENTRAL_TOKEN_PASSWORD` — Central Portal token password
   - `GPG_PRIVATE_KEY` — ASCII-armored GPG private key (`gpg --armor --export-secret-keys <keyid>`)
   - `GPG_PASSPHRASE` — passphrase for the GPG key
4. Publish GPG public key: `gpg --keyserver keyserver.ubuntu.com --send-keys <keyid>`

### Cleanup TODO
- Delete `.travis.yml` and `.travis/` directory after verifying workflows work

## Documentation
- `gh-pages/src/main/asciidoc/index.adoc` — full documentation with PlantUML/Graphviz diagrams
- Published at: http://goranstack.github.io/esox
