CREATE TABLE service.patient (
    id INT AUTO_INCREMENT PRIMARY KEY,
    prenom VARCHAR(50) NOT NULL,
    nom VARCHAR(50) NOT NULL,
    date_naissance DATE NOT NULL,
    genre ENUM('M', 'F') NOT NULL,
    adresse_postale VARCHAR(255),
    numero_telephone VARCHAR(15)
);

INSERT INTO service.patient (prenom, nom, date_naissance, genre, adresse_postale, numero_telephone)
VALUES
('Test', 'TestNone', '1966-12-31', 'F', '1 Brookside St', '100-222-3333'),
('Test', 'TestBorderline', '1945-06-24', 'M', '2 High St', '200-333-4444'),
('Test', 'TestInDanger', '2004-06-18', 'M', '3 Club Road', '300-444-5555'),
('Test', 'TestEarlyOnset', '2002-06-28', 'F', '4 Valley Dr', '400-555-6666');