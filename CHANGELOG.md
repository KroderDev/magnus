# Changelog

## [1.4.1](https://github.com/KroderDev/magnus/compare/v1.4.0...v1.4.1) (2026-07-17)


### Bug Fixes

* **fabric:** support remapped NBT runtime methods ([f0076a7](https://github.com/KroderDev/magnus/commit/f0076a77a18018736197fd4d7839d81821dc1b2a))


### Continuous Integration

* **release:** publish assets before immutable release ([4c2adbb](https://github.com/KroderDev/magnus/commit/4c2adbb03b3f19395cda604df6b365f23e90ecf7))

## [1.4.0](https://github.com/KroderDev/magnus/compare/v1.3.3...v1.4.0) (2026-07-14)


### Features

* increase signature timestamp tolerance to 60s and make configurable ([db9ecd0](https://github.com/KroderDev/magnus/commit/db9ecd03b03e492eb2f2bf1c1d75f5c14eea571e))
* **security:** increase signature timestamp tolerance and make configurable ([c21d9ba](https://github.com/KroderDev/magnus/commit/c21d9ba3f8b2f5e145a84255d3398fd3c3dc6c13))
* **server-state:** publish read-only server facts ([a1a1fcb](https://github.com/KroderDev/magnus/commit/a1a1fcbfe0d63b4ab1e50c035af71000d4ec9060))


### Bug Fixes

* **ci:** update labeler.yml syntax for actions/labeler v6 ([97681b9](https://github.com/KroderDev/magnus/commit/97681b9c0cf68af2cb8662a17cc276f7ca2bb5de))
* **deps:** update Testcontainers 2.0.5 module artifact names and harden pub/sub test synchronization ([8a1a208](https://github.com/KroderDev/magnus/commit/8a1a208bb49c652f60fe0c4062c685987031bba7))
* **security:** upgrade libexpat and p11-kit to resolve CVE-2026-56408 and CVE-2026-2100 ([0c7e36f](https://github.com/KroderDev/magnus/commit/0c7e36f1130b9557b7ec621921b56bb85e3769ba))
* **security:** upgrade libexpat to resolve CVE-2026-56408 ([7b599a3](https://github.com/KroderDev/magnus/commit/7b599a39f7cf06607f72629f9a665daddfe9173b))
* **security:** upgrade p11-kit and p11-kit-trust to resolve CVE-2026-2100 ([c905fc6](https://github.com/KroderDev/magnus/commit/c905fc64e18aa180549bda761b2ffdc091b684d5))


### Performance Improvements

* centralize Json instances across services and repositories ([c58fc2f](https://github.com/KroderDev/magnus/commit/c58fc2f3a90544efe16024cbe2b01879d2890e1f))
* centralize Json instances across services and repositories ([a375fe0](https://github.com/KroderDev/magnus/commit/a375fe0892191a285780f28fdc0f818a7e9f4f75))
* debounce cleanup iteration and short-circuit backup recovery checks ([7c603de](https://github.com/KroderDev/magnus/commit/7c603de2488664464f68b4839312052339006dbe))
* debounce stale entry cleanups and optimize backup scanning ([e77450d](https://github.com/KroderDev/magnus/commit/e77450d3001fa1189ee9f8b0ef8c6e586d9a7f1c))
* enable non-blocking async persistence and parallel batch saving ([dc4e4c9](https://github.com/KroderDev/magnus/commit/dc4e4c9ab5736f21966d38b3e1362a4885331136))
* enable non-blocking async persistence and parallel batch saving ([27f63cd](https://github.com/KroderDev/magnus/commit/27f63cd335d911e688ce21127916068a601751b1))
* optimize Redis connection pooling and pub/sub thread management ([1ad8686](https://github.com/KroderDev/magnus/commit/1ad868652d33919db1e1cf434b5397b44d5d35a5))
* optimize Redis connection pooling and pub/sub thread management ([428f607](https://github.com/KroderDev/magnus/commit/428f607d2ce179e2e2268f9424cca904d8b0f241))


### Documentation

* update README with signatureTimestampToleranceMs and 60s tolerance ([bfcdabf](https://github.com/KroderDev/magnus/commit/bfcdabf0c245836aeba8fe9d2bb0f4cc4fb23362))


### Tests

* stub hasAnyBackups and test short-circuit behavior in BackupRecoveryServiceTest ([4146c77](https://github.com/KroderDev/magnus/commit/4146c7775dec3a8d2a0f70bb44f20712d989bbf8))


### Build System

* **deps:** bump actions/labeler from 4 to 6 ([0f5df88](https://github.com/KroderDev/magnus/commit/0f5df88bc8cf535b5ce18c6e5b6dab7d81ea3feb))
* **deps:** bump actions/labeler from 4 to 6 ([156ffc1](https://github.com/KroderDev/magnus/commit/156ffc1ab968dfd13a6831e9d841e79b5e518975))
* **deps:** bump actions/upload-artifact from 4 to 7 ([8850869](https://github.com/KroderDev/magnus/commit/885086932a74be80b241fef0b56073deb00e662d))
* **deps:** bump actions/upload-artifact from 4 to 7 ([88531a4](https://github.com/KroderDev/magnus/commit/88531a49e666626b3029125659d8b657c15bd832))
* **deps:** bump org.testcontainers:testcontainers from 1.21.4 to 2.0.5 ([0d1f348](https://github.com/KroderDev/magnus/commit/0d1f34838c24a51813179f5ac99968a4dfce2cb9))
* **deps:** bump the minor-and-patch group across 1 directory with 3 updates ([057473c](https://github.com/KroderDev/magnus/commit/057473c5349b0e2e49ebf950052a4ae077bc4f20))
* **deps:** bump the minor-and-patch group across 1 directory with 3 updates ([e2e6644](https://github.com/KroderDev/magnus/commit/e2e6644992ecc2b037523c5158c24665ac0e3383))


### Miscellaneous Chores

* merge main to get labeler fix ([1f158c6](https://github.com/KroderDev/magnus/commit/1f158c6a803db593d2ef53ee751e8c3bac798307))
* normalize gradlew.bat line endings ([286b535](https://github.com/KroderDev/magnus/commit/286b535c1440e14389329bc7197b5979357384bb))

## [1.3.3](https://github.com/KroderDev/magnus/compare/v1.3.2...v1.3.3) (2026-07-01)


### Bug Fixes

* add @Suppress annotations on catch blocks with correct syntax, fix code complexity warnings ([44a122e](https://github.com/KroderDev/magnus/commit/44a122e7af810c51f7b3e18bab6336254e5aecf5))
* add blank line between [@file](https://github.com/file):Suppress and package in 2 test files (AnnotationOnSeparateLine) ([6a1c3dd](https://github.com/KroderDev/magnus/commit/6a1c3dd6e55857874cf6df1112f78197332a3cad))
* fix AnnotationOnSeparateLine, ImportOrdering groups, and test file imports ([547b2f6](https://github.com/KroderDev/magnus/commit/547b2f6b2d13ee0cfeaf259c1b12c5159d78ed77))
* fix excessive whitespace in catch blocks, ConfigLoader imports ([9ef31cf](https://github.com/KroderDev/magnus/commit/9ef31cfe22fc2f87873733d218fe629e163cb363))
* fix import ordering and indentation issues in test and main files ([109b34a](https://github.com/KroderDev/magnus/commit/109b34a4d75afd81331e455b01712e52678e8965))
* **minecraft:** split version-specific game test and metadata sources ([cae2741](https://github.com/KroderDev/magnus/commit/cae2741397ab1eef6e523857ce93adcbe0d6c560))
* **minecraft:** support 1.21.5+ NBT and gametest API changes ([62eaa74](https://github.com/KroderDev/magnus/commit/62eaa745063be8b50409036c109d0396022cb1d3))
* move catch to same line as closing brace (SpacingAroundKeyword) ([69a6267](https://github.com/KroderDev/magnus/commit/69a6267073bbaac4f317121003b21c95fe538b35))
* normalize gradlew.bat to LF per .gitattributes ([ee7718b](https://github.com/KroderDev/magnus/commit/ee7718bc5c1eae854e067dbe31cfe8491734b5a3))
* **release-please:** align manifest component with existing tag format ([2f44765](https://github.com/KroderDev/magnus/commit/2f44765aba3e11b0a402ca671f0bbc5f764b6700))
* resolve build compilation errors and remaining detekt lint violations ([e149041](https://github.com/KroderDev/magnus/commit/e14904115cd306c5dee7877973aeb44bb250239e))
* restore missing closing brace in BackupRecoveryService else block ([33338d2](https://github.com/KroderDev/magnus/commit/33338d27d59982c99911cd7ca6b439a91fa6246b))
* revert mockk to wildcard imports in test files, add missing Database import ([8964dce](https://github.com/KroderDev/magnus/commit/8964dce53e0393c92324365dedfa7d80be6c8542))
* suppress ImportOrdering warnings, add missing LoggerFactory import ([f04efc5](https://github.com/KroderDev/magnus/commit/f04efc5d44d6f28b5b2274b33d11ba091c2f3f2f))
* suppress NoWildcardImports in test files with mockk wildcard imports ([f6605b2](https://github.com/KroderDev/magnus/commit/f6605b2e8e496decb16b9ed4245cd82a95902990))
* suppress TooGenericExceptionCaught, SwallowedException, PrintStackTrace; fix formatting issues ([e7a4a23](https://github.com/KroderDev/magnus/commit/e7a4a23c4da6f62048fefc4b165c7f810c8c4f83))
* use [@file](https://github.com/file):Suppress before package instead of per-catch annotations ([5c5aafe](https://github.com/KroderDev/magnus/commit/5c5aafe10ec969fdb01ba4798c30e499107abf6a))
* use correct rule ID WildcardImport for [@file](https://github.com/file):Suppress, fix blank lines and newlines ([64bcb26](https://github.com/KroderDev/magnus/commit/64bcb2698ccc2c881a34f24439461a2fe1a47bcf))


### Documentation

* add pull request template ([5416d8a](https://github.com/KroderDev/magnus/commit/5416d8a157c653ccb55bf8e508919ee5af5df9fe))
* reorder badges by importance ([3ec77e2](https://github.com/KroderDev/magnus/commit/3ec77e20e143f64d4de41f0fe9795c382e8e4716))
* update badges with PostgreSQL, Redis, Trivy and fix build badge branch ([cb9651e](https://github.com/KroderDev/magnus/commit/cb9651e2146487a641cea582a4108009a905ad99))
* update Minecraft version badge to show supported range (1.21.1–1.21.11) ([d735e9e](https://github.com/KroderDev/magnus/commit/d735e9e240736dd13f30b18f41117f1b1d6d3bc9))
* update project description to reflect modular architecture ([e9fd121](https://github.com/KroderDev/magnus/commit/e9fd121c9e3bdda6e45efae6139d9ce0fb863193))


### Continuous Integration

* add changelog sections for all commit types used in project ([0269b3b](https://github.com/KroderDev/magnus/commit/0269b3b02fd0e036cbbf75fa0f627bb141ba876e))
* add CodeQL analysis via Java bytecode extraction ([6d78060](https://github.com/KroderDev/magnus/commit/6d780606a4bbc0150995ff03d80ff7e87c83f96d))
* add Dependabot configuration for Gradle, Docker, and GitHub Actions ([6f88cd0](https://github.com/KroderDev/magnus/commit/6f88cd07804f88f2680402a1c8bd88da67e315df))
* add Fabric compatibility build matrix ([aa4387c](https://github.com/KroderDev/magnus/commit/aa4387c9fe6990ce9ad27cb97c9bddf98b295d43))
* add GitHub Actions workflow for labeling PRs ([45efa7f](https://github.com/KroderDev/magnus/commit/45efa7f17d982970dee078f4de6cebab2e810298))
* add labeler workflow and config ([b39f120](https://github.com/KroderDev/magnus/commit/b39f120566f1e2b7176664c2ebda9e285a93e840))
* add release-please with multi-version Modrinth/CurseForge publishing ([c0a40bb](https://github.com/KroderDev/magnus/commit/c0a40bbc5425e764376cde01492887f415edb324))
* add standalone detekt lint workflow ([5408d6d](https://github.com/KroderDev/magnus/commit/5408d6d356ea8eeb8acd4f56b42dd52fc2c80295))
* avoid duplicate build runs on PR pushes ([8a3ff8c](https://github.com/KroderDev/magnus/commit/8a3ff8cac797e3c01865298931a1d5282eb0bc7f))
* **codeql:** switch to autobuild mode for proper Java extraction ([cdfb008](https://github.com/KroderDev/magnus/commit/cdfb008cee912443c3064701dcc588d603dff961))
* keep 1.21.11 matrix checks experimental ([0db25f9](https://github.com/KroderDev/magnus/commit/0db25f9262dbb380bdeba7457d43a1c486f456f7))
* **release-please:** wrap config in packages section for explicit component matching ([1663690](https://github.com/KroderDev/magnus/commit/1663690bf6889feea8278c4943f815ca8fa4db3b))
* **release:** publish tested Minecraft versions ([fa0fa6d](https://github.com/KroderDev/magnus/commit/fa0fa6d2b128b65a8a39195ff0707202c3307821))
* remove CodeQL, doesn't support Kotlin 2.4.0 yet ([d33fa09](https://github.com/KroderDev/magnus/commit/d33fa099780e62fd2736753dd4295edcea0847dd))


### Build System

* add Docker-based dev environment with Detekt linting ([2e3546d](https://github.com/KroderDev/magnus/commit/2e3546dcadd0681b9ab4c42c9ed23eef6dedfcc0))
* **deps:** bump actions/checkout from 4 to 7 ([1259c34](https://github.com/KroderDev/magnus/commit/1259c34ddad681706c5911977ca64bb59101a415))
* **deps:** bump actions/checkout from 4 to 7 ([bdc6ace](https://github.com/KroderDev/magnus/commit/bdc6acef53fb35122b06960b1e044a6488598d48))
* **deps:** bump actions/setup-java from 4 to 5 ([f0793a8](https://github.com/KroderDev/magnus/commit/f0793a883fdcd39e756adc6515a2c5d624bb5c6f))
* **deps:** bump actions/setup-java from 4 to 5 ([18f9cd4](https://github.com/KroderDev/magnus/commit/18f9cd41727f295b4ba98550c6e5eee44a741ed6))
* **deps:** bump aquasecurity/trivy-action from 0.5.0 to 0.36.0 ([bdf04f4](https://github.com/KroderDev/magnus/commit/bdf04f46e3aefd21a662558a1f1f515474e014cc))
* **deps:** bump aquasecurity/trivy-action from 0.5.0 to 0.36.0 ([8cb43d2](https://github.com/KroderDev/magnus/commit/8cb43d23e090e80dd4e8fc57cfd4c83d57f8c719))
* **deps:** bump eclipse-temurin from 21-jdk-alpine to 25-jdk-alpine ([6d274ef](https://github.com/KroderDev/magnus/commit/6d274ef1bb077796cee23522f403785b2ab26ce4))
* **deps:** bump eclipse-temurin from 21-jdk-alpine to 25-jdk-alpine ([1c5eef1](https://github.com/KroderDev/magnus/commit/1c5eef14c5dbf47caf98a95f1f51ff976822748f))
* **deps:** bump github/codeql-action from 3 to 4 ([33c943b](https://github.com/KroderDev/magnus/commit/33c943b4c54f2846d372adc383852deac43be35f))
* **deps:** bump github/codeql-action from 3 to 4 ([fe45c9b](https://github.com/KroderDev/magnus/commit/fe45c9bc9965845f6f66d64ee7b8ed2a671c448e))
* **deps:** bump gradle/actions from 3 to 6 ([0a02903](https://github.com/KroderDev/magnus/commit/0a02903f0833fa2d7d8e14a41200c6e8b8d1ddcd))
* **deps:** bump gradle/actions from 3 to 6 ([a5974dc](https://github.com/KroderDev/magnus/commit/a5974dcb6c10155bf736a22664d8fa437771aa04))
* **deps:** bump org.junit.jupiter:junit-jupiter from 5.10.0 to 6.1.1 ([4a22a69](https://github.com/KroderDev/magnus/commit/4a22a69757b6b5fd8a567e819c67c1b481193f7c))
* **deps:** bump org.junit.jupiter:junit-jupiter from 5.10.0 to 6.1.1 ([fe2ad53](https://github.com/KroderDev/magnus/commit/fe2ad53813fec25f332ed49ee2914950afc6d0ec))
* **deps:** bump the minor-and-patch group with 9 updates ([2767aab](https://github.com/KroderDev/magnus/commit/2767aab10807d931d69bea28912754e5dbb3497a))
* **deps:** bump the minor-and-patch group with 9 updates ([df3d073](https://github.com/KroderDev/magnus/commit/df3d07384f196bf312d07b766f509ca420dc3da0))


### Miscellaneous Chores

* add Trivy scan for dependency and secret detection ([5ba4d9f](https://github.com/KroderDev/magnus/commit/5ba4d9ffc3fabf0dfac09546f04ecb81731079b0))


### Code Refactoring

* fix all detekt lint violations ([2e4044f](https://github.com/KroderDev/magnus/commit/2e4044f1b3c32bfe5d28438bbed6dbbf9d1a257e))
