

const filePath = process.argv[2];
const version = process.argv[3];

if (!filePath) {
    console.log("missing file path argument");
    exit(1)
}

const fs = require('fs');
const packageJson = require(filePath);

if (version)
packageJson.version = version;
packageJson.scripts.build = "ng build --output-path target/classes/static";

fs.writeFileSync(filePath, JSON.stringify(packageJson, null, 2));