CREATE SCHEMA IF NOT EXISTS chroniclesOfArtifacts;

USE chroniclesOfArtifacts;

CREATE TABLE IF NOT EXISTS classes(
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    nameClass VARCHAR(50) NOT NULL,
    functionClass VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS categoryArmors(
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    typeArmor VARCHAR(20) NOT NULL,
    categoryArmor VARCHAR(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS categoryWeapons(
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    categoryWeapon VARCHAR(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS properties(
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	property VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS weapons(
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    weapon VARCHAR(50) NOT NULL,
    proficiency INT NOT NULL,
    damage VARCHAR(10) NOT NULL,
    rangeWeapon VARCHAR(10) NOT NULL,
    numberOfHands INT NOT NULL,
    category INT NOT NULL,
    FOREIGN KEY (category) REFERENCES categoryWeapons(id)
);

CREATE TABLE IF NOT EXISTS armors(
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    armor VARCHAR(50) NOT NULL,
    ca INT NOT NULL,
    penalty INT NOT NULL,
    displacement INT NOT NULL,
    category INT NOT NULL,
    FOREIGN KEY (category) REFERENCES categoryArmors(id)
);

CREATE TABLE IF NOT EXISTS weaponClasses(
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    classId INT NOT NULL,
    catWeaponId INT NOT NULL,
    FOREIGN KEY (catWeaponId) REFERENCES categoryWeapons(id),
    FOREIGN KEY (classID) REFERENCES classes(id)
);

CREATE TABLE IF NOT EXISTS armorClasses(
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    catArmorId INT NOT NULL,
    classId INT NOT NULL,
    FOREIGN KEY (catArmorId) REFERENCES categoryArmors(id),
    FOREIGN KEY (classID) REFERENCES classes(id)
);

CREATE TABLE IF NOT EXISTS weaponProperties(
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	propertyId INT NOT NULL,
    weaponId INT NOT NULL,
    FOREIGN KEY (propertyId) REFERENCES properties(id),
    FOREIGN KEY (weaponId) REFERENCES weapons(id)
);

CREATE TABLE IF NOT EXISTS artifactWeapons(
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    artifact VARCHAR(50) NOT NULL,
    descWeapon VARCHAR(255) NOT NULL,
    typeWeapon INT NOT NULL,
    skill VARCHAR(255) NOT NULL,
    price FLOAT NOT NULL,
    weightWeapon FLOAT NOT NULL,
    bonusAtk INT NOT NULL,
    bonusDamage INT NOT NULL,
    registerDate DATETIME NOT NULL,
    FOREIGN KEY (typeWeapon) REFERENCES weapons(id)
);

CREATE TABLE IF NOT EXISTS artifactArmors(
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    artifact VARCHAR(50) NOT NULL,
    descArmor VARCHAR(255) NOT NULL,
    typeArmor INT NOT NULL,
    skill VARCHAR(255) NOT NULL,
    price FLOAT NOT NULL,
    weightArmor FLOAT NOT NULL,
    bonusDefense INT NOT NULL,
    registerDate DATETIME NOT NULL,
    FOREIGN KEY (typeArmor) REFERENCES armors(id)
);

CREATE TABLE IF NOT EXISTS artifactItems(
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    artifact VARCHAR(50) NOT NULL,
    descItem VARCHAR(255) NOT NULL,
    skill VARCHAR(255) NOT NULL,
    price FLOAT NOT NULL,
    weightItem FLOAT NOT NULL,
    bodySection VARCHAR(50) NOT NULL,
    registerDate DATETIME NOT NULL
);

INSERT INTO chroniclesOfArtifacts.classes(nameClass, functionClass)
VALUES
("guerreiro", "defensor"),
("paladino", "defensor"),
("bardo", "líder"),
("clérigo", "líder"),
("senhor da guerra", "líder"),
("mago", "controlador"),
("druida", "controlador"),
("feiticeiro", "agressor"),
("monge", "agressor"),
("bárbaro", "agressor"),
("bruxo", "agressor"),
("ladino", "agressor"),
("patrulheiro", "agressor");

INSERT INTO chroniclesOfArtifacts.categoryArmors(typeArmor, categoryArmor)
VALUES
("traje", "leve"),
("corselete", "leve"),
("gibão", "leve"),
("cota", "pesada"),
("brunea", "pesada"),
("placas", "pesada"),
("escudo", "escudo");

INSERT INTO chroniclesOfArtifacts.categoryWeapons(categoryWeapon)
VALUES
("corpo a corpo simples"),
("corpo a corpo militares"),
("corpo a corpo superiores"),
("à distância simples"),
("à distância militares"),
("à distância superiores");

INSERT INTO chroniclesOfArtifacts.properties(property)
VALUES
("nenhuma"),
("mão inábil"),
("leve de arremesso"),
("pesada de arremesso"),
("versátil"),
("decisivo elevado"),
("alcance"),
("recarga livre"),
("recarta mínima"),
("pequena");

INSERT INTO chroniclesOfArtifacts.weapons(weapon, category, proficiency, damage, rangeWeapon, numberOfHands)
VALUES
("adaga" , 1, 3,  "1d4", "5/10", 1),
("azagaia" , 1, 2,  "1d6", "10/20", 1),
("clava" , 1, 2,  "1d6", "1", 1),
("foice" , 1, 2,  "1d6", "1", 1),
("lança curta" , 1, 2,  "1d8", "1", 1),
("maça" , 1, 2,  "1d8", "1", 1),
("bordão" , 1, 2,  "1d8", "1", 2),
("clava grande" , 1, 2,  "2d4", "1", 2),
("maça estrela" , 1, 2,  "1d10", "1", 2),
("segadeira" , 1, 2,  "2d4", "1", 2),
("cimitarra" , 2, 2,  "1d8", "1", 1),
("espada curta" , 2, 3,  "1d6", "1", 1),
("espada longa" , 2, 3,  "1d8", "1", 1),
("machadinha" , 2, 2,  "1d6", "5/10", 1),
("machado de batalha" , 2, 2,  "1d10", "1", 1),
("mangual" , 2, 2,  "1d10", "1", 1),
("martelo de arremesso" , 2, 2,  "1d6", "5/10", 1),
("martelo de guerra" , 2, 2,  "1d10", "1", 1),
("picareta de guerra" , 2, 2,  "1d8", "1", 1),
("alabarda" , 2, 2,  "1d10", "2", 2),
("alfange" , 2, 3,  "2d4", "1", 2),
("espada grande" , 2, 3,  "1d10", "1", 2),
("glaive" , 2, 2,  "2d4", "2", 2),
("lança longa" , 2, 2,  "1d10", "2", 2),
("machado grande" , 2, 2,  "1d12", "1", 2),
("malho" , 2, 2,  "2d6", "1", 2),
("mangual pesado" , 2, 2,  "2d6", "1", 2),
("espada bastarda" , 3, 3,  "1d10", "1", 1),
("katar" , 3, 3,  "1d6", "1", 1),
("rapieira" , 3, 3,  "1d8", "1", 1),
("corrente com cravos" , 3, 3,  "2d4", "2", 1),
("besta de mão" , 4, 2,  "1d6", "10/20", 1),
("funda" , 4, 2,  "1d6", "10/20", 1),
("besta" , 4, 2,  "1d8", "15/30", 2),
("arco curto" , 5, 2,  "1d8", "15/30", 2),
("arco longo" , 5, 2,  "1d10", "20/40", 2),
("shuriken" , 6, 3,  "1d4", "6/12", 1);

INSERT INTO chroniclesOfArtifacts.armors(armor, ca, penalty, displacement, category)
VALUES
("traje de tecido (roupa básica)" , 0, 0, 0, 1),
("traje feérico" , 1, 0, 0, 1),
("traje celestial" , 2, 0, 0, 1),
("corselete de couro" , 2, 0, 0, 2),
("corselete feérico" , 3, 0, 0, 2),
("corselete celestial" , 4, 0, 0, 2),
("gibão de peles" , 3, -1, 0, 3),
("gibão negro" , 4, -1, 0, 3),
("gibão ancestral" , 5, -1, 0, 3),
("cota de malha" , 6, -1, -1, 4),
("cota forjada" , 9, -1, -1, 4),
("cota espiritual" , 12, -1, -1, 4),
("brunea" , 7, 0, -1, 5),
("brunea dracônica" , 10, 0, -1, 5),
("brunea ancestral" , 13, 0, -1, 5),
("armadura de placas" , 8, -2, -1, 6),
("placas de guerra" , 11, -2, -1, 6),
("placas divinas" , 14, -2, -1, 6),
("escudo leve" , 1, 0, 0, 7),
("escudo pesado", 2, -2, 0, 7);

INSERT INTO chroniclesOfArtifacts.weaponProperties(weaponId, propertyId)
VALUES
(1, 2),
(1, 3),
(2, 4),
(3, 1),
(4, 2),
(5, 5),
(6, 5),
(7, 1),
(8, 1),
(9, 1),
(10, 1),
(11, 6),
(12, 2),
(13, 5),
(14, 2),
(14, 4),
(15, 5),
(16, 5),
(17, 2),
(17, 4),
(18, 5),
(19, 5),
(19, 6),
(20, 7),
(21, 6),
(22, 1),
(23, 7),
(24, 7),
(25, 6),
(26, 1),
(27, 1),
(28, 5),
(29, 2),
(29, 6),
(30, 1),
(31, 7),
(32, 8),
(33, 8),
(34, 9),
(35, 8),
(35, 10),
(36, 8),
(37, 3);

INSERT INTO chroniclesOfArtifacts.weaponClasses(classId, catWeaponId)
VALUES
(1, 1),
(1, 2),
(1, 4),
(1, 5),
(2, 1),
(2, 2),
(2, 4),
(4, 1),
(4, 4),
(5, 1),
(5, 2),
(5, 4),
(6, 1),
(12, 1),
(12, 4),
(12, 6),
(13, 1),
(13, 2),
(13, 4),
(13, 5);

INSERT INTO chroniclesOfArtifacts.armorClasses(classId, catArmorId)
VALUES
(11, 1),
(11, 2),
(12, 1),
(12, 2),
(4, 1),
(4, 2),
(4, 3),
(4, 4),
(13, 1),
(13, 2),
(13, 3),
(1, 1),
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(1, 5),
(1, 7),
(2, 1),
(2, 2),
(2, 3),
(2, 4),
(2, 5),
(2, 6),
(2, 7),
(5, 1),
(5, 2),
(5, 3),
(5, 4),
(5, 7);
