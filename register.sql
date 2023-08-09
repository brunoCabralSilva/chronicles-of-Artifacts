CREATE SCHEMA IF NOT EXISTS chroniclesOfArtifacts;

USE chroniclesOfArtifacts;

CREATE TABLE IF NOT EXISTS typeItem(
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	typeItem VARCHAR(50) NOT NULL,
	bodySection VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS classes(
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    nameClass VARCHAR(50) NOT NULL,
    functionClass VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS items(
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    nameItem VARCHAR(50) NOT NULL,
    descriptionItem VARCHAR(255) NOT NULL,
    skillItem VARCHAR(255) NOT NULL,
    weight DOUBLE NOT NULL,
    price DOUBLE NOT NULL,
    registerDate DATE NOT NULL,
    typeItem INT NOT NULL,
    FOREIGN KEY (typeItem) REFERENCES typeItem(id)
);

CREATE TABLE IF NOT EXISTS itemsClasses(
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    itemId INT NOT NULL,
    classId INT NOT NULL,
    FOREIGN KEY (itemId) REFERENCES items(id),
    FOREIGN KEY (classID) REFERENCES classes(id)
);

INSERT INTO chroniclesOfArtifacts.typeItem(typeItem, bodySection)
VALUES
("elmo", "cabeça"),
("pingente", "pescoço"),
("armadura", "peitoral"),
("arma", "uma mão"),
("arma", "duas mãos"),
("manto", "costas"),
("luvas", "mãos"),
("anel", "dedos"),
("cinto", "cintura"),
("botas", "pés"),
("flutuante", "não consome espaços corporais");

INSERT INTO chroniclesOfArtifacts.weapons(weapon, categoryWeapon, proficiency, damage, rangeWeapon, group, properties, numberOfHands)
VALUES
("adaga" , "corpo a corpo simples", 3,  "1d4", "5/10", "lâmina Leve", "mão Inábil, leve de arremesso", 1),
("azagaia" , "corpo a corpo simples", 2,  "1d6", "10/20", "lança", "pesada de arremesso", 1),
("clava" , "corpo a corpo simples", 2,  "1d6", "1", "maça", "", 1),
("foice" , "corpo a corpo simples", 2,  "1d6", "1", "lâmina leve", "mão inábil", 1),
("lança curta" , "corpo a corpo simples", 2,  "1d8", "1", "lança", "versátil", 1),
("maça" , "corpo a corpo simples", 2,  "1d8", "1", "maça", "versátil", 1),
("bordão" , "corpo a corpo simples", 2,  "1d8", "1", "bordão", "", 2),
("clava grande" , "corpo a corpo simples", 2,  "2d4", "1", "maça", "", 2),
("maça estrela" , "corpo a corpo simples", 2,  "1d10", "1", "maça", "", 2),
("segadeira" , "corpo a corpo simples", 2,  "2d4", "1", "lâmina pesada", "", 2),
("cimitarra" , "corpo a corpo militares", 2,  "1d8", "1", "lâmina pesada", "decisivo elevado", 1),
("espada curta" , "corpo a corpo militares", 3,  "1d6", "1", "lâmina leve", "mão inábil", 1),
("espada longa" , "corpo a corpo militares", 3,  "1d8", "1", "lâmina pesada", "versátil", 1),
("machadinha" , "corpo a corpo militares", 2,  "1d6", "5/10", "machado", "mão inábil, pesada de arremesso", 1),
("machado de batalha" , "corpo a corpo militares", 2,  "1d10", "1", "machado", "versátil", 1),
("mangual" , "corpo a corpo militares", 2,  "1d10", "1", "mangual", "versátil", 1),
("martelo de arremesso" , "corpo a corpo militares", 2,  "1d6", "5/10", "martelo", "mão inábil, pesada de arremesso", 1),
("martelo de guerra" , "corpo a corpo militares", 2,  "1d10", "1", "martelo", "versátil", 1),
("picareta de guerra" , "corpo a corpo militares", 2,  "1d8", "1", "picareta", "decisivo elevado, versátil", 1),
("alabarda" , "corpo a corpo militares", 2,  "1d10", "2", "picareta", "alcance", 2),
("alfange" , "corpo a corpo militares", 3,  "2d4", "1", "picareta", "decisivo elevado", 2),
("espada grande" , "corpo a corpo militares", 3,  "1d10", "1", "picareta", "", 2),
("glaive" , "corpo a corpo militares", 2,  "2d4", "2", "picareta", "alcance", 2),
("lança longa" , "corpo a corpo militares", 2,  "1d10", "2", "picareta", "alcance", 2),
("machado grande" , "corpo a corpo militares", 2,  "1d12", "1", "picareta", "decisivo elevado", 2),
("malho" , "corpo a corpo militares", 2,  "2d6", "1", "picareta", "", 2),
("mangual pesado" , "corpo a corpo militares", 2,  "2d6", "1", "picareta", "", 2),
("espada bastarda" , "corpo a corpo superiores", 3,  "1d10", "1", "lâmina leve", "versátil", 1),
("katar" , "corpo a corpo superiores", 3,  "1d6", "1", "lâmina leve", "decisivo elevado, mão inábil", 1),
("rapieira" , "corpo a corpo superiores", 3,  "1d8", "1", "lâmina leve", "", 1),
("corrente com cravos" , "corpo a corpo superiores", 3,  "2d4", "2", "mangual", "alcance", 1),
("besta de mão" , "à distância simples", 2,  "1d6", "10/20", "besta", "recarga livre", 1),
("funda" , "à distância simples", 2,  "1d6", "10/20", "atiradeira", "recarga livre", 1),
("besta" , "à distância simples", 2,  "1d8", "15/30", "besta", "recarga mínima", 2),
("arco curto" , "à distância militares", 2,  "1d8", "15/30", "arco", "pequena, recarga livre", 2),
("arco longo" , "à distância militares", 2,  "1d10", "20/40", "arco", "recarga livre", 2),
("shuriken" , "à distância superiores", 3,  "1d4", "6/12", "", "", 1),

INSERT INTO chroniclesOfArtifacts.combatFunctions(nameFunction)
VALUES
("agressor"),
("defensor"),
("líder"),
("controlador");

INSERT INTO chroniclesOfArtifacts.classes(nameClass, functionClass)
VALUES
("guerreiro", 2),
("paladino", 2),
("bardo", 3),
("clérigo", 3),
("mago", 4),
("druida", 4),
("feiticeiro", 1),
("monge", 1),
("bárbaro", 1),
("bruxo", 1),
("patrulheiro", 1);