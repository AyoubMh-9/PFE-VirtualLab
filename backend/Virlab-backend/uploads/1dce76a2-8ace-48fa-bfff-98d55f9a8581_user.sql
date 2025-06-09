-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : mer. 28 mai 2025 à 17:16
-- Version du serveur : 10.4.32-MariaDB
-- Version de PHP : 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `virtual_lab`
--

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

CREATE TABLE `user` (
  `dtype` varchar(31) NOT NULL,
  `id` bigint(20) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('ADMIN','CLIENT','TECHNICIEN') NOT NULL,
  `username` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `user`
--

INSERT INTO `user` (`dtype`, `id`, `email`, `password`, `role`, `username`) VALUES
('ADMIN', 2, 'admin@virtual-lab.com', '$2a$12$F7RjK9wTTGuV6.prTqqfauUCiulykmNgGHc1NHpkq1wveCfXeFk1m', 'ADMIN', 'Ayoub Mouhib'),
('CLIENT', 3, 'chorouk1mouhib@gmail.com', '$2a$12$t/A8zplBWhGnR5R6y.5E5u6Spn4OKxXnNpews7Ql3z1.MG7G34KhC', 'CLIENT', 'Chorouk Mouhib'),
('TECHNICIAN', 4, 'Sara@gmai.lcom', '$2a$12$/7VeGsqUSNWUYf7EjSsjzeRbO3LgKoMjNFUKRkXwzyXKTPTdu7EMy', 'TECHNICIEN', 'Sara Mouhib'),
('CLIENT', 5, 'Bader@gmail.com', '$2a$12$k..WV.yADt1p5i0u7R0k4uPAOLzBByJa5ztZ8GBkE8CxUEhsgJm6q', 'CLIENT', 'Bader Bader'),
('TECHNICIAN', 6, 'Mohamed@gmail.com', '$2a$12$sjpeOiJBQcl6aXOeUKxlWurjtooEu7Gmdf0M8GvS0VNJx6zuW.2om', 'TECHNICIEN', 'Mohamed Mouhib');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKob8kqyqqgmefl0aco34akdtpe` (`email`),
  ADD UNIQUE KEY `UKsb8bbouer5wak8vyiiy4pf2bx` (`username`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `user`
--
ALTER TABLE `user`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
