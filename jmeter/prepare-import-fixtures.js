const fs = require('fs');

// Helper function to generate a random string of numbers for passportID
function generateRandomPassportID(length = 10) {
  return Date.now();
}

// Static data structure
function createStaticObject(passportID) {
  return {
    name: generateRandomPassportID().toString(),
    coordinates: {
      x: 13.37,
      y: 42
    },
    creationDate: "2024-11-16T14:57:42.183+00:00",
    cave: {
      numberOfTreasures: 314
    },
    killer: passportID === null ? null : {
      name: "Dambldor",
      eyeColor: "GREEN",
      hairColor: "GREEN",
      location: {
        x: 404,
        y: 500.5,
        name: "Берлога_Забугорного"
      },
      birthday: "1988-10-10T21:00:00.000+00:00",
      height: 170.2,
      passportID: generateRandomPassportID()
    },
    age: 666,
    color: "BLACK",
    type: "FIRE",
    character: "EVIL",
    head: {
      size: 7
    },
    canBeEditedByAdmin: true
  };
}

// Generate the JSON data
const jsonData = [];
jsonData.push(createStaticObject(true));
setTimeout(() => {
  jsonData.push(createStaticObject(true));
  const outputFileName = 'import-1.json';
  fs.writeFileSync(outputFileName, JSON.stringify(jsonData, null, 2), 'utf8');
  console.log(`JSON files created: ${outputFileName}`);
}, 1000);
