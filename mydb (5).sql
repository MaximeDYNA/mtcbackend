-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le :  jeu. 26 nov. 2020 à 07:35
-- Version du serveur :  5.7.26
-- Version de PHP :  5.6.40

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `mydb`
--

-- --------------------------------------------------------

--
-- Structure de la table `energie`
--

DROP TABLE IF EXISTS `energie`;
CREATE TABLE IF NOT EXISTS `energie` (
  `energie_id` bigint(20) NOT NULL,
  `active_status` tinyint(1) DEFAULT '1',
  `created_date` datetime DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `libelle` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`energie_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `hibernate_sequence`
--

DROP TABLE IF EXISTS `hibernate_sequence`;
CREATE TABLE IF NOT EXISTS `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `hibernate_sequence`
--

INSERT INTO `hibernate_sequence` (`next_val`) VALUES
(785),
(785),
(785),
(785),
(785),
(785),
(785),
(785),
(785),
(785),
(785),
(785),
(785),
(785),
(785),
(785),
(785);

-- --------------------------------------------------------

--
-- Structure de la table `t_adresse`
--

DROP TABLE IF EXISTS `t_adresse`;
CREATE TABLE IF NOT EXISTS `t_adresse` (
  `adresse_id` bigint(20) NOT NULL,
  `created_date` bigint(20) NOT NULL,
  `modified_date` bigint(20) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `nom` varchar(255) DEFAULT NULL,
  `division_pays_division_pays_id` bigint(20) DEFAULT NULL,
  `organisation_organisation_id` bigint(20) DEFAULT NULL,
  `pays_pays_id` bigint(20) DEFAULT NULL,
  `active_status` varchar(255) DEFAULT NULL,
  `partenaire_partenaire_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`adresse_id`),
  KEY `FKen7rgd80o52vt6qh5n0boq533` (`division_pays_division_pays_id`),
  KEY `FKbj6co7futohp2f73tv8rd9p0t` (`organisation_organisation_id`),
  KEY `FKmw2hcqcfx40tk1vf70aen62i6` (`pays_pays_id`),
  KEY `FKetf7cxpx9xuyfd8urkcpfhcb2` (`partenaire_partenaire_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `t_caisse`
--

DROP TABLE IF EXISTS `t_caisse`;
CREATE TABLE IF NOT EXISTS `t_caisse` (
  `caisse_id` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `id_organisation` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`caisse_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `t_caissier`
--

DROP TABLE IF EXISTS `t_caissier`;
CREATE TABLE IF NOT EXISTS `t_caissier` (
  `caissier_id` varchar(255) NOT NULL,
  `code_caissier` varchar(255) DEFAULT NULL,
  `id_organisation` varchar(255) DEFAULT NULL,
  `partenaire_partenaire_id` bigint(20) DEFAULT NULL,
  `id_utilisateur` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`caissier_id`),
  KEY `FKb5vs21xqb41toy45rerufeiqn` (`partenaire_partenaire_id`),
  KEY `FK1yxnow4g3apqsp77qv45u18c6` (`id_utilisateur`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `t_caissier`
--

INSERT INTO `t_caissier` (`caissier_id`, `code_caissier`, `id_organisation`, `partenaire_partenaire_id`, `id_utilisateur`) VALUES
('1', 'code', '1', 1, 1);

-- --------------------------------------------------------

--
-- Structure de la table `t_caissiercaisse`
--

DROP TABLE IF EXISTS `t_caissiercaisse`;
CREATE TABLE IF NOT EXISTS `t_caissiercaisse` (
  `caissier_caisse_id` varchar(255) NOT NULL,
  `id_organisation` varchar(255) DEFAULT NULL,
  `caisse_id` varchar(255) DEFAULT NULL,
  `caissier_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`caissier_caisse_id`),
  KEY `FK7g63caai3y82w4ib88f4d355t` (`caisse_id`),
  KEY `FKigub17h2wha6t91kx8escg301` (`caissier_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `t_cartegrise`
--

DROP TABLE IF EXISTS `t_cartegrise`;
CREATE TABLE IF NOT EXISTS `t_cartegrise` (
  `carte_grise_id` bigint(20) NOT NULL,
  `active_status` tinyint(1) DEFAULT '1',
  `created_date` datetime DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `centre_ssdt` varchar(255) DEFAULT NULL,
  `commune` varchar(255) DEFAULT NULL,
  `date_debut_valid` datetime DEFAULT NULL,
  `date_delivrance` datetime DEFAULT NULL,
  `date_fin_valid` datetime DEFAULT NULL,
  `enregistrement` varchar(255) DEFAULT NULL,
  `genre_vehicule` varchar(255) DEFAULT NULL,
  `lieu_dedelivrance` varchar(255) DEFAULT NULL,
  `montant_paye` double NOT NULL,
  `num_immatriculation` varchar(255) DEFAULT NULL,
  `pre_immatriculation` varchar(255) DEFAULT NULL,
  `ssdt_id` varchar(255) DEFAULT NULL,
  `vehicule_gage` bit(1) NOT NULL,
  `produit_produit_id` bigint(20) DEFAULT NULL,
  `proprietaire_vehicule_proprietaire_vehicule_id` bigint(20) DEFAULT NULL,
  `vehicule_vehicule_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`carte_grise_id`),
  KEY `FKn1yfvlchuge0up4subrluej11` (`produit_produit_id`),
  KEY `FKngt91bigrgp300yjgkpihfvvu` (`proprietaire_vehicule_proprietaire_vehicule_id`),
  KEY `FK26x8hvnfltyke14adj6xfgj1a` (`vehicule_vehicule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `t_cartegrise`
--

INSERT INTO `t_cartegrise` (`carte_grise_id`, `active_status`, `created_date`, `modified_date`, `centre_ssdt`, `commune`, `date_debut_valid`, `date_delivrance`, `date_fin_valid`, `enregistrement`, `genre_vehicule`, `lieu_dedelivrance`, `montant_paye`, `num_immatriculation`, `pre_immatriculation`, `ssdt_id`, `vehicule_gage`, `produit_produit_id`, `proprietaire_vehicule_proprietaire_vehicule_id`, `vehicule_vehicule_id`) VALUES
(2, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 10000, 'LT1512FE', NULL, NULL, b'0', 2, 1, 54),
(52, 0, '2020-11-17 13:02:31', '2020-11-21 13:38:38', '543', '', '2020-11-18 00:00:00', '2020-11-21 00:00:00', NULL, '', '', '2020-11-21', 0, '12458', '12345', '122520', b'0', 2, 1, 641),
(56, 0, '2020-11-17 16:07:32', '2020-11-17 16:07:32', '543', 'BP DOUALA', '2020-11-18 00:00:00', '2020-11-28 00:00:00', '2020-11-25 00:00:00', '14SE', '1', '2020-11-08', 1200, 'LT306LE', '12345', '122520', b'0', 2, 40, 55),
(146, 0, '2020-11-20 11:12:48', '2020-11-20 11:12:48', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'HGHGYU', NULL, NULL, b'0', 7, NULL, NULL),
(149, 0, '2020-11-20 11:12:48', '2020-11-20 11:12:48', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'LLKLJHG', NULL, NULL, b'0', 3, NULL, NULL),
(157, 0, '2020-11-20 11:17:26', '2020-11-20 11:17:26', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, '?KJKJ', NULL, NULL, b'0', 2, NULL, NULL),
(160, 0, '2020-11-20 11:17:26', '2020-11-20 11:17:26', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'HGHGYUE', NULL, NULL, b'0', 2, NULL, NULL),
(170, 0, '2020-11-20 11:24:55', '2020-11-20 11:24:55', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'QWERT', NULL, NULL, b'0', 2, NULL, NULL),
(173, 0, '2020-11-20 11:24:55', '2020-11-20 11:24:55', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'QWERTS', NULL, NULL, b'0', 7, NULL, NULL),
(180, 0, '2020-11-20 11:29:20', '2020-11-20 11:29:20', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'ASDFGH', NULL, NULL, b'0', 2, NULL, NULL),
(183, 0, '2020-11-20 11:29:20', '2020-11-20 11:29:20', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'ASDFGI', NULL, NULL, b'0', 7, NULL, NULL),
(202, 0, '2020-11-20 12:03:00', '2020-11-20 12:03:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'LLKLJH52', NULL, NULL, b'0', 7, NULL, NULL),
(205, 0, '2020-11-20 12:03:01', '2020-11-20 12:03:01', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'LLKJH52', NULL, NULL, b'0', 6, NULL, NULL),
(212, 0, '2020-11-20 12:08:36', '2020-11-20 12:08:36', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'LT350DEA', NULL, NULL, b'0', 2, NULL, NULL),
(215, 0, '2020-11-20 12:08:36', '2020-11-20 12:08:36', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'LT35EA11', NULL, NULL, b'0', 4, NULL, NULL),
(223, 0, '2020-11-20 12:19:08', '2020-11-20 12:19:08', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'ASDFGHJ', NULL, NULL, b'0', 2, NULL, NULL),
(226, 0, '2020-11-20 12:19:08', '2020-11-20 12:19:08', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'ASRFGHJ', NULL, NULL, b'0', 4, NULL, NULL),
(233, 0, '2020-11-20 12:22:21', '2020-11-20 12:22:21', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'ASDFG', NULL, NULL, b'0', 2, NULL, NULL),
(236, 0, '2020-11-20 12:22:22', '2020-11-20 12:22:22', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'TTTT', NULL, NULL, b'0', 3, NULL, NULL),
(241, 0, '2020-11-20 12:22:43', '2020-11-20 12:22:43', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'ASDFG', NULL, NULL, b'0', 2, NULL, NULL),
(244, 0, '2020-11-20 12:22:43', '2020-11-20 12:22:43', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'TTTT', NULL, NULL, b'0', 3, NULL, NULL),
(251, 0, '2020-11-20 12:23:51', '2020-11-20 12:23:51', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'ASDFG', NULL, NULL, b'0', 2, NULL, NULL),
(254, 0, '2020-11-20 12:23:51', '2020-11-20 12:23:51', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'TTTT', NULL, NULL, b'0', 3, NULL, NULL),
(257, 0, '2020-11-20 12:23:51', '2020-11-20 12:23:51', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'EEFFRRR', NULL, NULL, b'0', 3, NULL, NULL),
(260, 0, '2020-11-20 12:23:52', '2020-11-20 12:23:52', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'EEFFDD', NULL, NULL, b'0', 4, NULL, NULL),
(267, 0, '2020-11-20 12:25:46', '2020-11-20 12:25:46', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'ASXXC', NULL, NULL, b'0', 3, NULL, NULL),
(270, 0, '2020-11-20 12:25:46', '2020-11-20 12:25:46', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'AAED', NULL, NULL, b'0', 3, NULL, NULL),
(277, 0, '2020-11-20 12:28:39', '2020-11-20 12:28:39', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'ADDCC', NULL, NULL, b'0', 2, NULL, NULL),
(280, 0, '2020-11-20 12:28:39', '2020-11-20 12:28:39', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'ADECC', NULL, NULL, b'0', 5, NULL, NULL),
(289, 0, '2020-11-20 12:35:19', '2020-11-20 12:35:19', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'sqqq', NULL, NULL, b'0', 3, NULL, NULL),
(292, 0, '2020-11-20 12:35:20', '2020-11-20 12:35:20', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'SQQw', NULL, NULL, b'0', 4, NULL, NULL),
(295, 0, '2020-11-20 12:35:20', '2020-11-20 12:35:20', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'QQSSXXD', NULL, NULL, b'0', 3, NULL, NULL),
(298, 0, '2020-11-20 12:35:20', '2020-11-20 12:35:20', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'QQESXXD', NULL, NULL, b'0', 7, NULL, NULL),
(305, 0, '2020-11-20 12:38:42', '2020-11-20 12:38:42', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'ZXCVED', NULL, NULL, b'0', 2, NULL, NULL),
(308, 0, '2020-11-20 12:38:42', '2020-11-20 12:38:42', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'ZXCTRED', NULL, NULL, b'0', 3, NULL, NULL),
(315, 0, '2020-11-20 12:50:48', '2020-11-20 12:50:48', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'lt347ol', NULL, NULL, b'0', 3, NULL, NULL),
(318, 0, '2020-11-20 12:50:48', '2020-11-20 12:50:48', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'AA', NULL, NULL, b'0', 2, NULL, NULL),
(324, 0, '2020-11-20 12:51:40', '2020-11-20 12:51:40', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'AAS', NULL, NULL, b'0', 2, NULL, NULL),
(330, 0, '2020-11-20 12:55:48', '2020-11-20 12:55:48', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'AXC', NULL, NULL, b'0', 2, NULL, NULL),
(336, 0, '2020-11-20 12:57:33', '2020-11-20 12:57:33', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'ss', NULL, NULL, b'0', 2, NULL, NULL),
(342, 0, '2020-11-20 12:58:35', '2020-11-20 12:58:35', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'ss', NULL, NULL, b'0', 2, NULL, NULL),
(345, 0, '2020-11-20 12:58:35', '2020-11-20 12:58:35', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'FFR', NULL, NULL, b'0', 2, NULL, NULL),
(355, 0, '2020-11-20 13:20:48', '2020-11-20 13:20:48', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'SWd', NULL, NULL, b'0', 4, NULL, NULL),
(358, 0, '2020-11-20 13:20:48', '2020-11-20 13:20:48', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'SWDZw', NULL, NULL, b'0', 4, NULL, NULL),
(449, 0, '2020-11-20 14:01:36', '2020-11-20 14:01:36', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'mloki12', NULL, NULL, b'0', 7, NULL, NULL),
(452, 0, '2020-11-20 14:01:36', '2020-11-20 14:01:36', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'lkmk', NULL, NULL, b'0', 2, NULL, NULL),
(455, 0, '2020-11-20 14:01:36', '2020-11-20 14:01:36', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'LKMK10', NULL, NULL, b'0', 4, NULL, NULL),
(458, 0, '2020-11-20 14:01:36', '2020-11-20 14:01:36', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'dd', NULL, NULL, b'0', 2, NULL, NULL),
(461, 0, '2020-11-20 14:01:36', '2020-11-20 14:01:36', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 's', NULL, NULL, b'0', 2, NULL, NULL),
(464, 0, '2020-11-20 14:01:36', '2020-11-20 14:01:36', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'sdcsds', NULL, NULL, b'0', 4, NULL, NULL),
(467, 0, '2020-11-20 14:01:36', '2020-11-20 14:01:36', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'SDCSDS1', NULL, NULL, b'0', 5, NULL, NULL),
(470, 0, '2020-11-20 14:01:36', '2020-11-20 14:01:36', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'SDCSDS144', NULL, NULL, b'0', 2, NULL, NULL),
(473, 0, '2020-11-20 14:01:36', '2020-11-20 14:01:36', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'c', NULL, NULL, b'0', 2, NULL, NULL),
(476, 0, '2020-11-20 14:01:37', '2020-11-20 14:01:37', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'asd', NULL, NULL, b'0', 2, NULL, NULL),
(479, 0, '2020-11-20 14:01:37', '2020-11-20 14:01:37', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'njiu', NULL, NULL, b'0', 4, NULL, NULL),
(482, 0, '2020-11-20 14:01:37', '2020-11-20 14:01:37', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'qg', NULL, NULL, b'0', 2, NULL, NULL),
(488, 0, '2020-11-20 14:15:51', '2020-11-20 14:15:51', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'SD', NULL, NULL, b'0', 2, NULL, NULL),
(494, 0, '2020-11-20 14:16:40', '2020-11-20 14:16:40', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'SD', NULL, NULL, b'0', 2, NULL, NULL),
(497, 0, '2020-11-20 14:16:40', '2020-11-20 14:16:40', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'DD', NULL, NULL, b'0', 2, NULL, NULL),
(503, 0, '2020-11-20 14:55:59', '2020-11-20 14:55:59', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'yjnyjnyjn', NULL, NULL, b'0', 2, NULL, NULL),
(516, 0, '2020-11-20 15:22:33', '2020-11-20 15:22:33', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'dd', NULL, NULL, b'0', 2, NULL, NULL),
(519, 0, '2020-11-20 15:22:33', '2020-11-20 15:22:33', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'dfg', NULL, NULL, b'0', 2, NULL, NULL),
(526, 0, '2020-11-20 15:25:19', '2020-11-20 15:25:19', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'd', NULL, NULL, b'0', 2, NULL, NULL),
(534, 0, '2020-11-20 15:37:57', '2020-11-20 15:37:57', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'fg', NULL, NULL, b'0', 3, NULL, NULL),
(540, 0, '2020-11-20 15:38:54', '2020-11-20 15:38:54', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'd', NULL, NULL, b'0', 2, NULL, NULL),
(547, 0, '2020-11-20 15:39:41', '2020-11-20 15:39:41', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'd', NULL, NULL, b'0', 2, NULL, NULL),
(550, 0, '2020-11-20 15:39:42', '2020-11-20 15:39:42', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'dd', NULL, NULL, b'0', 2, NULL, NULL),
(556, 0, '2020-11-20 15:42:13', '2020-11-20 15:42:13', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'wedf', NULL, NULL, b'0', 2, NULL, NULL),
(564, 0, '2020-11-20 15:56:38', '2020-11-20 15:56:38', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'Xx', NULL, NULL, b'0', 2, NULL, NULL),
(570, 0, '2020-11-20 15:59:14', '2020-11-20 15:59:14', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'vh', NULL, NULL, b'0', 2, NULL, NULL),
(578, 0, '2020-11-20 16:00:24', '2020-11-20 16:00:24', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'dd', NULL, NULL, b'0', 3, NULL, NULL),
(585, 0, '2020-11-20 16:01:24', '2020-11-20 16:01:24', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'DDs', NULL, NULL, b'0', 2, NULL, NULL),
(588, 0, '2020-11-20 16:01:24', '2020-11-20 16:01:24', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 's', NULL, NULL, b'0', 2, NULL, NULL),
(595, 0, '2020-11-20 16:01:54', '2020-11-20 16:01:54', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'd', NULL, NULL, b'0', 3, NULL, NULL),
(604, 0, '2020-11-21 09:31:26', '2020-11-21 09:31:26', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'fff', NULL, NULL, b'0', 3, NULL, NULL),
(610, 0, '2020-11-21 09:53:04', '2020-11-21 09:53:04', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'fff', NULL, NULL, b'0', 3, NULL, NULL),
(613, 0, '2020-11-21 09:53:04', '2020-11-21 09:53:04', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'ff', NULL, NULL, b'0', 3, NULL, NULL),
(622, 0, '2020-11-21 09:58:36', '2020-11-21 09:58:36', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'f', NULL, NULL, b'0', 2, NULL, NULL),
(631, 0, '2020-11-21 10:03:03', '2020-11-21 10:03:03', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'ba', NULL, NULL, b'0', 3, NULL, NULL),
(661, 0, '2020-11-24 09:05:09', '2020-11-24 09:05:09', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'NW 683 BA', NULL, NULL, b'0', 6, NULL, NULL),
(670, 0, '2020-11-24 09:21:29', '2020-11-24 09:21:29', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'LT642JA', NULL, NULL, b'0', 3, NULL, NULL),
(685, 0, '2020-11-24 10:52:06', '2020-11-24 10:52:06', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, '1000000', NULL, NULL, b'0', 1, NULL, NULL),
(690, 0, '2020-11-24 11:53:01', '2020-11-24 11:53:01', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'ugyyfvgt', NULL, NULL, b'0', 1, NULL, NULL),
(773, 0, '2020-11-25 16:54:02', '2020-11-25 16:54:02', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'DEFR', NULL, NULL, b'0', 3, NULL, NULL),
(776, 0, '2020-11-25 16:54:03', '2020-11-25 16:54:03', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'DEFRDAZ', NULL, NULL, b'0', 4, NULL, NULL),
(782, 0, '2020-11-25 17:46:23', '2020-11-25 17:46:23', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'er', NULL, NULL, b'0', 2, NULL, NULL);

-- --------------------------------------------------------

--
-- Structure de la table `t_categorieproduit`
--

DROP TABLE IF EXISTS `t_categorieproduit`;
CREATE TABLE IF NOT EXISTS `t_categorieproduit` (
  `categorie_produit_id` bigint(20) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `libelle` varchar(255) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `active_status` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`categorie_produit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `t_categorieproduit`
--

INSERT INTO `t_categorieproduit` (`categorie_produit_id`, `description`, `libelle`, `created_date`, `modified_date`, `active_status`) VALUES
(1, 'Vignette automobile', 'vignette', '2020-11-01 00:00:00', '2020-11-02 00:00:00', 1),
(2, 'Assurance Auto', 'Assurances Auto', '2020-11-01 00:00:00', '2020-11-02 00:00:00', 1),
(17, 'Un decaissement est une operation de debit d\'une somme dans le compte', 'dec', '2020-11-13 15:50:52', '2020-11-13 15:50:52', 1);

-- --------------------------------------------------------

--
-- Structure de la table `t_categorietest`
--

DROP TABLE IF EXISTS `t_categorietest`;
CREATE TABLE IF NOT EXISTS `t_categorietest` (
  `id_categorie_test` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `libelle` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_categorie_test`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `t_categorietestmachine`
--

DROP TABLE IF EXISTS `t_categorietestmachine`;
CREATE TABLE IF NOT EXISTS `t_categorietestmachine` (
  `id_categorie_test_machine` varchar(255) NOT NULL,
  `id_organisation` varchar(255) DEFAULT NULL,
  `idcategorietest` varchar(255) DEFAULT NULL,
  `id_machine` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_categorie_test_machine`),
  KEY `FK2u2k6qto5o0vy70lisb3bxqqs` (`idcategorietest`),
  KEY `FKct34i49dnwqouievc6t3uoiak` (`id_machine`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `t_client`
--

DROP TABLE IF EXISTS `t_client`;
CREATE TABLE IF NOT EXISTS `t_client` (
  `client_id` bigint(20) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `partenaire_partenaire_id` bigint(20) DEFAULT NULL,
  `active_status` tinyint(1) DEFAULT '1',
  `created_date` datetime DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  PRIMARY KEY (`client_id`),
  KEY `FK2ckb50s00267ade3ukshgbmy9` (`partenaire_partenaire_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `t_client`
--

INSERT INTO `t_client` (`client_id`, `description`, `partenaire_partenaire_id`, `active_status`, `created_date`, `modified_date`) VALUES
(1, NULL, 1, 1, NULL, NULL),
(2, NULL, 0, 1, NULL, NULL);

-- --------------------------------------------------------

--
-- Structure de la table `t_contact`
--

DROP TABLE IF EXISTS `t_contact`;
CREATE TABLE IF NOT EXISTS `t_contact` (
  `contact_id` bigint(20) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `partenaire_partenaire_id` bigint(20) DEFAULT NULL,
  `client_client_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`contact_id`),
  KEY `FKr81l195f851wgays13e165dkk` (`partenaire_partenaire_id`),
  KEY `FK2uqfos85e0qp7gstaanr7rwts` (`client_client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `t_contact`
--

INSERT INTO `t_contact` (`contact_id`, `description`, `partenaire_partenaire_id`, `client_client_id`) VALUES
(0, NULL, 0, 1),
(125, NULL, 124, NULL),
(655, NULL, 654, NULL),
(666, NULL, 665, NULL);

-- --------------------------------------------------------

--
-- Structure de la table `t_controleur`
--

DROP TABLE IF EXISTS `t_controleur`;
CREATE TABLE IF NOT EXISTS `t_controleur` (
  `id_controleur` bigint(20) NOT NULL,
  `agremment` varchar(255) DEFAULT NULL,
  `score` int(11) NOT NULL,
  `partenaire_partenaire_id` bigint(20) DEFAULT NULL,
  `utilisateur_utilisateur_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id_controleur`),
  KEY `FKgmpbobpbsfqerix60g5infpx1` (`partenaire_partenaire_id`),
  KEY `FKo0oml59pvrsxht3co2d40xwr7` (`utilisateur_utilisateur_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `t_controleur`
--

INSERT INTO `t_controleur` (`id_controleur`, `agremment`, `score`, `partenaire_partenaire_id`, `utilisateur_utilisateur_id`) VALUES
(1, '42425', 100, 1, 1);

-- --------------------------------------------------------

--
-- Structure de la table `t_detailvente`
--

DROP TABLE IF EXISTS `t_detailvente`;
CREATE TABLE IF NOT EXISTS `t_detailvente` (
  `id_detail_vente` bigint(20) NOT NULL,
  `reference` varchar(255) DEFAULT NULL,
  `id_produit` bigint(20) DEFAULT NULL,
  `vente_id_vente` bigint(20) DEFAULT NULL,
  `active_status` tinyint(1) DEFAULT '1',
  `created_date` datetime DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id_detail_vente`),
  KEY `FKsw05awqtjrm9bb8x5j7bo9mv7` (`id_produit`),
  KEY `FKqscesvpcsvhogcxslgdxi64ai` (`vente_id_vente`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `t_detailvente`
--

INSERT INTO `t_detailvente` (`id_detail_vente`, `reference`, `id_produit`, `vente_id_vente`, `active_status`, `created_date`, `modified_date`) VALUES
(1, NULL, 2, 1, 1, NULL, NULL),
(145, 'HGHGYU', 7, 143, 0, '2020-11-20 11:12:48', '2020-11-20 11:12:48'),
(148, 'LLKLJHG', 3, 143, 0, '2020-11-20 11:12:48', '2020-11-20 11:12:48'),
(156, '?KJKJ', 2, 154, 0, '2020-11-20 11:17:26', '2020-11-20 11:17:26'),
(159, 'HGHGYUE', 2, 154, 0, '2020-11-20 11:17:26', '2020-11-20 11:17:26'),
(169, 'QWERT', 2, 167, 0, '2020-11-20 11:24:55', '2020-11-20 11:24:55'),
(172, 'QWERTS', 7, 167, 0, '2020-11-20 11:24:55', '2020-11-20 11:24:55'),
(179, 'ASDFGH', 2, 177, 0, '2020-11-20 11:29:20', '2020-11-20 11:29:20'),
(182, 'ASDFGI', 7, 177, 0, '2020-11-20 11:29:20', '2020-11-20 11:29:20'),
(201, 'LLKLJH52', 7, 199, 0, '2020-11-20 12:03:00', '2020-11-20 12:03:00'),
(204, 'LLKJH52', 6, 199, 0, '2020-11-20 12:03:01', '2020-11-20 12:03:01'),
(211, 'LT350DEA', 2, 209, 0, '2020-11-20 12:08:36', '2020-11-20 12:08:36'),
(214, 'LT35EA11', 4, 209, 0, '2020-11-20 12:08:36', '2020-11-20 12:08:36'),
(222, 'ASDFGHJ', 2, 220, 0, '2020-11-20 12:19:08', '2020-11-20 12:19:08'),
(225, 'ASRFGHJ', 4, 220, 0, '2020-11-20 12:19:08', '2020-11-20 12:19:08'),
(232, 'ASDFG', 2, 230, 0, '2020-11-20 12:22:21', '2020-11-20 12:22:21'),
(235, 'TTTT', 3, 230, 0, '2020-11-20 12:22:22', '2020-11-20 12:22:22'),
(240, 'ASDFG', 2, 238, 0, '2020-11-20 12:22:43', '2020-11-20 12:22:43'),
(243, 'TTTT', 3, 238, 0, '2020-11-20 12:22:43', '2020-11-20 12:22:43'),
(250, 'ASDFG', 2, 248, 0, '2020-11-20 12:23:51', '2020-11-20 12:23:51'),
(253, 'TTTT', 3, 248, 0, '2020-11-20 12:23:51', '2020-11-20 12:23:51'),
(256, 'EEFFRRR', 3, 248, 0, '2020-11-20 12:23:51', '2020-11-20 12:23:51'),
(259, 'EEFFDD', 4, 248, 0, '2020-11-20 12:23:52', '2020-11-20 12:23:52'),
(266, 'ASXXC', 3, 264, 0, '2020-11-20 12:25:46', '2020-11-20 12:25:46'),
(269, 'AAED', 3, 264, 0, '2020-11-20 12:25:46', '2020-11-20 12:25:46'),
(276, 'ADDCC', 2, 274, 0, '2020-11-20 12:28:39', '2020-11-20 12:28:39'),
(279, 'ADECC', 5, 274, 0, '2020-11-20 12:28:39', '2020-11-20 12:28:39'),
(288, 'sqqq', 3, 286, 0, '2020-11-20 12:35:19', '2020-11-20 12:35:19'),
(291, 'SQQw', 4, 286, 0, '2020-11-20 12:35:20', '2020-11-20 12:35:20'),
(294, 'QQSSXXD', 3, 286, 0, '2020-11-20 12:35:20', '2020-11-20 12:35:20'),
(297, 'QQESXXD', 7, 286, 0, '2020-11-20 12:35:20', '2020-11-20 12:35:20'),
(304, 'ZXCVED', 2, 302, 0, '2020-11-20 12:38:42', '2020-11-20 12:38:42'),
(307, 'ZXCTRED', 3, 302, 0, '2020-11-20 12:38:42', '2020-11-20 12:38:42'),
(314, 'lt347ol', 3, 312, 0, '2020-11-20 12:50:48', '2020-11-20 12:50:48'),
(317, 'AA', 2, 312, 0, '2020-11-20 12:50:48', '2020-11-20 12:50:48'),
(323, 'AAS', 2, 321, 0, '2020-11-20 12:51:40', '2020-11-20 12:51:40'),
(329, 'AXC', 2, 327, 0, '2020-11-20 12:55:48', '2020-11-20 12:55:48'),
(335, 'ss', 2, 333, 0, '2020-11-20 12:57:33', '2020-11-20 12:57:33'),
(341, 'ss', 2, 339, 0, '2020-11-20 12:58:35', '2020-11-20 12:58:35'),
(344, 'FFR', 2, 339, 0, '2020-11-20 12:58:35', '2020-11-20 12:58:35'),
(354, 'SWd', 4, 352, 0, '2020-11-20 13:20:48', '2020-11-20 13:20:48'),
(357, 'SWDZw', 4, 352, 0, '2020-11-20 13:20:48', '2020-11-20 13:20:48'),
(448, 'mloki12', 7, 446, 0, '2020-11-20 14:01:36', '2020-11-20 14:01:36'),
(451, 'lkmk', 2, 446, 0, '2020-11-20 14:01:36', '2020-11-20 14:01:36'),
(454, 'LKMK10', 4, 446, 0, '2020-11-20 14:01:36', '2020-11-20 14:01:36'),
(457, 'dd', 2, 446, 0, '2020-11-20 14:01:36', '2020-11-20 14:01:36'),
(460, 's', 2, 446, 0, '2020-11-20 14:01:36', '2020-11-20 14:01:36'),
(463, 'sdcsds', 4, 446, 0, '2020-11-20 14:01:36', '2020-11-20 14:01:36'),
(466, 'SDCSDS1', 5, 446, 0, '2020-11-20 14:01:36', '2020-11-20 14:01:36'),
(469, 'SDCSDS144', 2, 446, 0, '2020-11-20 14:01:36', '2020-11-20 14:01:36'),
(472, 'c', 2, 446, 0, '2020-11-20 14:01:36', '2020-11-20 14:01:36'),
(475, 'asd', 2, 446, 0, '2020-11-20 14:01:37', '2020-11-20 14:01:37'),
(478, 'njiu', 4, 446, 0, '2020-11-20 14:01:37', '2020-11-20 14:01:37'),
(481, 'qg', 2, 446, 0, '2020-11-20 14:01:37', '2020-11-20 14:01:37'),
(487, 'SD', 2, 485, 0, '2020-11-20 14:15:51', '2020-11-20 14:15:51'),
(493, 'SD', 2, 491, 0, '2020-11-20 14:16:40', '2020-11-20 14:16:40'),
(496, 'DD', 2, 491, 0, '2020-11-20 14:16:40', '2020-11-20 14:16:40'),
(502, 'yjnyjnyjn', 2, 500, 0, '2020-11-20 14:55:58', '2020-11-20 14:55:58'),
(515, 'dd', 2, 513, 0, '2020-11-20 15:22:33', '2020-11-20 15:22:33'),
(518, 'dfg', 2, 513, 0, '2020-11-20 15:22:33', '2020-11-20 15:22:33'),
(525, 'd', 2, 523, 0, '2020-11-20 15:25:19', '2020-11-20 15:25:19'),
(533, 'fg', 3, 531, 0, '2020-11-20 15:37:57', '2020-11-20 15:37:57'),
(539, 'd', 2, 537, 0, '2020-11-20 15:38:54', '2020-11-20 15:38:54'),
(546, 'd', 2, 544, 0, '2020-11-20 15:39:41', '2020-11-20 15:39:41'),
(549, 'dd', 2, 544, 0, '2020-11-20 15:39:41', '2020-11-20 15:39:41'),
(555, 'wedf', 2, 553, 0, '2020-11-20 15:42:13', '2020-11-20 15:42:13'),
(563, 'Xx', 2, 561, 0, '2020-11-20 15:56:38', '2020-11-20 15:56:38'),
(569, 'vh', 2, 567, 0, '2020-11-20 15:59:14', '2020-11-20 15:59:14'),
(577, 'dd', 3, 575, 0, '2020-11-20 16:00:24', '2020-11-20 16:00:24'),
(584, 'DDs', 2, 582, 0, '2020-11-20 16:01:24', '2020-11-20 16:01:24'),
(587, 's', 2, 582, 0, '2020-11-20 16:01:24', '2020-11-20 16:01:24'),
(594, 'd', 3, 592, 0, '2020-11-20 16:01:54', '2020-11-20 16:01:54'),
(603, 'fff', 3, 601, 0, '2020-11-21 09:31:26', '2020-11-21 09:31:26'),
(609, 'fff', 3, 607, 0, '2020-11-21 09:53:04', '2020-11-21 09:53:04'),
(612, 'ff', 3, 607, 0, '2020-11-21 09:53:04', '2020-11-21 09:53:04'),
(621, 'f', 2, 619, 0, '2020-11-21 09:58:36', '2020-11-21 09:58:36'),
(630, 'ba', 3, 628, 0, '2020-11-21 10:03:03', '2020-11-21 10:03:03'),
(660, 'NW 683 BA', 6, 658, 0, '2020-11-24 09:05:09', '2020-11-24 09:05:09'),
(669, 'LT642JA', 3, 667, 0, '2020-11-24 09:21:29', '2020-11-24 09:21:29'),
(684, '1000000', 1, 682, 0, '2020-11-24 10:52:06', '2020-11-24 10:52:06'),
(689, 'ugyyfvgt', 1, 688, 0, '2020-11-24 11:53:01', '2020-11-24 11:53:01'),
(772, 'DEFR', 3, 770, 0, '2020-11-25 16:54:02', '2020-11-25 16:54:02'),
(775, 'DEFRDAZ', 4, 770, 0, '2020-11-25 16:54:02', '2020-11-25 16:54:02'),
(781, 'er', 2, 779, 0, '2020-11-25 17:46:23', '2020-11-25 17:46:23');

-- --------------------------------------------------------

--
-- Structure de la table `t_divisionpays`
--

DROP TABLE IF EXISTS `t_divisionpays`;
CREATE TABLE IF NOT EXISTS `t_divisionpays` (
  `division_pays_id` bigint(20) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `id_division_pays_parent` varchar(255) DEFAULT NULL,
  `libelle` varchar(255) DEFAULT NULL,
  `pays_pays_id` bigint(20) DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `active_status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`division_pays_id`),
  KEY `FKba9kogqnj8b8n15s0ctm6xtcq` (`pays_pays_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `t_divisionpays`
--

INSERT INTO `t_divisionpays` (`division_pays_id`, `description`, `id_division_pays_parent`, `libelle`, `pays_pays_id`, `modified_date`, `created_date`, `active_status`) VALUES
(5, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(6, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(9, 'Adamaoua centre', NULL, 'Ngaoundéré', 16, '2020-11-12 13:47:04', '2020-11-12 13:25:27', NULL);

-- --------------------------------------------------------

--
-- Structure de la table `t_energie`
--

DROP TABLE IF EXISTS `t_energie`;
CREATE TABLE IF NOT EXISTS `t_energie` (
  `energie_id` bigint(20) NOT NULL,
  `active_status` tinyint(1) DEFAULT '1',
  `created_date` datetime DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `libelle` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`energie_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `t_energie`
--

INSERT INTO `t_energie` (`energie_id`, `active_status`, `created_date`, `modified_date`, `libelle`) VALUES
(1, 1, '2020-11-16 00:00:00', '2020-11-16 00:00:00', 'essence'),
(2, 1, '2020-11-16 00:00:00', '2020-11-16 00:00:00', 'Diesel'),
(3, 1, '2020-11-16 00:00:00', '2020-11-16 00:00:00', 'kérozène');

-- --------------------------------------------------------

--
-- Structure de la table `t_hold`
--

DROP TABLE IF EXISTS `t_hold`;
CREATE TABLE IF NOT EXISTS `t_hold` (
  `hold_id` bigint(20) NOT NULL,
  `number` bigint(20) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  `session_caisse_session_caisse_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`hold_id`),
  UNIQUE KEY `UK_dub7hb6vr7ns0tiaefs5ubvks` (`number`),
  KEY `FKeoiccvimdi16sscg8ggj0fvk` (`session_caisse_session_caisse_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `t_hold`
--

INSERT INTO `t_hold` (`hold_id`, `number`, `time`, `session_caisse_session_caisse_id`) VALUES
(657, 2, '2020-11-24 09:02:22', 656),
(784, 1, '1970-01-01 17:46:23', 651);

-- --------------------------------------------------------

--
-- Structure de la table `t_inspection`
--

DROP TABLE IF EXISTS `t_inspection`;
CREATE TABLE IF NOT EXISTS `t_inspection` (
  `id_inspection` bigint(20) NOT NULL,
  `active_status` tinyint(1) DEFAULT '1',
  `created_date` datetime DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `chassis` varchar(255) DEFAULT NULL,
  `date_debut` datetime DEFAULT NULL,
  `date_fin` datetime DEFAULT NULL,
  `essieux` int(11) NOT NULL,
  `kilometrage` double NOT NULL,
  `position` varchar(255) DEFAULT NULL,
  `signature` varchar(255) DEFAULT NULL,
  `controleur_id_controleur` bigint(20) DEFAULT NULL,
  `ligne_id_ligne` bigint(20) DEFAULT NULL,
  `produit_produit_id` bigint(20) DEFAULT NULL,
  `visite_id_visite` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id_inspection`),
  KEY `FKdb8d13pyc050eax4h72owwxvx` (`ligne_id_ligne`),
  KEY `FKqd2x540t65gib4n2whvuuqxh9` (`produit_produit_id`),
  KEY `FKo421h7b2s8q85p2htfyfqt1cv` (`visite_id_visite`),
  KEY `FKfc0aql5mi49uy2d8m5talqjvr` (`controleur_id_controleur`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `t_inspection`
--

INSERT INTO `t_inspection` (`id_inspection`, `active_status`, `created_date`, `modified_date`, `chassis`, `date_debut`, `date_fin`, `essieux`, `kilometrage`, `position`, `signature`, `controleur_id_controleur`, `ligne_id_ligne`, `produit_produit_id`, `visite_id_visite`) VALUES
(65, 0, '2020-11-18 13:37:20', '2020-11-18 13:37:20', '56782', NULL, NULL, 4, 54871, '1,', NULL, 1, 1, 2, 2),
(66, 0, '2020-11-18 13:38:40', '2020-11-18 13:38:40', '56782', NULL, NULL, 4, 5487, '1,', NULL, 1, 1, 2, 2),
(67, 0, '2020-11-18 13:44:13', '2020-11-18 13:44:13', '12356', NULL, NULL, 4, 54871, '1,', NULL, 1, 1, 2, 1),
(68, 0, '2020-11-18 13:45:22', '2020-11-18 13:45:22', '2500125', NULL, NULL, 4, 5487, '1,', NULL, 1, 1, 2, 1),
(69, 0, '2020-11-18 13:47:18', '2020-11-18 13:47:18', '56782', NULL, NULL, 4, 54871, '1,', NULL, 1, 1, 2, 1),
(70, 0, '2020-11-18 13:49:09', '2020-11-18 13:49:09', '12356', NULL, NULL, 4, 54871, '1,', NULL, 1, 1, 2, 1),
(71, 0, '2020-11-18 13:50:40', '2020-11-18 13:50:40', '12356', NULL, NULL, 5, 5487, '1,', NULL, 1, 1, 2, 1),
(72, 0, '2020-11-18 13:50:48', '2020-11-18 13:50:48', '12356', NULL, NULL, 5, 5487, '1,', NULL, 1, 1, 2, 1),
(73, 0, '2020-11-18 13:51:28', '2020-11-18 13:51:28', '1235602', NULL, NULL, 4, 54870, '1,', NULL, 1, 1, 2, 1),
(74, 0, '2020-11-18 14:27:28', '2020-11-18 14:27:28', '1235620', NULL, NULL, 4, 5669960, '1,', NULL, 1, 1, 2, 2),
(75, 0, '2020-11-18 14:29:31', '2020-11-18 14:29:31', '56782', NULL, NULL, 5, 54871, '1,', NULL, 1, 1, 2, 1),
(77, 0, '2020-11-18 15:22:33', '2020-11-18 15:22:33', '', NULL, NULL, 3, 5669960, '1,', NULL, 1, 1, 2, 1),
(78, 0, '2020-11-18 16:01:11', '2020-11-18 16:01:11', '56782', NULL, NULL, 4, 5669960, '1,', NULL, 1, 1, 2, 2),
(79, 0, '2020-11-18 16:01:25', '2020-11-18 16:01:25', '56782', NULL, NULL, 4, 5669960, '1,', NULL, 1, 1, 2, 1),
(80, 0, '2020-11-18 16:02:45', '2020-11-18 16:02:45', '56782', NULL, NULL, 4, 54871, '1,', NULL, 1, 1, 2, 1),
(81, 0, '2020-11-18 16:03:35', '2020-11-18 16:03:35', '56782', NULL, NULL, 4, 5487, '1,', NULL, 1, 1, 2, 2),
(82, 0, '2020-11-18 16:03:45', '2020-11-18 16:03:45', '56782', NULL, NULL, 0, 5487, '1,', NULL, 1, 1, 2, 1);

-- --------------------------------------------------------

--
-- Structure de la table `t_ligne`
--

DROP TABLE IF EXISTS `t_ligne`;
CREATE TABLE IF NOT EXISTS `t_ligne` (
  `id_ligne` bigint(20) NOT NULL,
  `active_status` tinyint(1) DEFAULT '1',
  `created_date` datetime DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `id_organisation` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_ligne`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `t_ligne`
--

INSERT INTO `t_ligne` (`id_ligne`, `active_status`, `created_date`, `modified_date`, `description`, `id_organisation`) VALUES
(1, 1, NULL, NULL, 'première ligne', '1');

-- --------------------------------------------------------

--
-- Structure de la table `t_lignemachine`
--

DROP TABLE IF EXISTS `t_lignemachine`;
CREATE TABLE IF NOT EXISTS `t_lignemachine` (
  `ligne_machine` varchar(255) NOT NULL,
  `id_organisation` varchar(255) DEFAULT NULL,
  `id_ligne` bigint(20) DEFAULT NULL,
  `id_machine` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ligne_machine`),
  KEY `FKehrgix9rl6bov18xohebfpq54` (`id_ligne`),
  KEY `FKempadxvkep051090703xcfp22` (`id_machine`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `t_machine`
--

DROP TABLE IF EXISTS `t_machine`;
CREATE TABLE IF NOT EXISTS `t_machine` (
  `id_machine` varchar(255) NOT NULL,
  `fabriquant` varchar(255) DEFAULT NULL,
  `id_organisation` varchar(255) DEFAULT NULL,
  `model` varchar(255) DEFAULT NULL,
  `num_serie` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_machine`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `t_marquevehicule`
--

DROP TABLE IF EXISTS `t_marquevehicule`;
CREATE TABLE IF NOT EXISTS `t_marquevehicule` (
  `marque_vehicule_id` bigint(20) NOT NULL,
  `active_status` tinyint(1) DEFAULT '1',
  `created_date` datetime DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `libelle` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`marque_vehicule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `t_marquevehicule`
--

INSERT INTO `t_marquevehicule` (`marque_vehicule_id`, `active_status`, `created_date`, `modified_date`, `description`, `libelle`) VALUES
(1, 1, NULL, NULL, 'Yaris', 'Toyota'),
(2, 1, NULL, NULL, 'Golf', 'Wolfwagen'),
(3, 1, NULL, NULL, '4Matic', 'Mercedes');

-- --------------------------------------------------------

--
-- Structure de la table `t_mesure`
--

DROP TABLE IF EXISTS `t_mesure`;
CREATE TABLE IF NOT EXISTS `t_mesure` (
  `id_mesure` varchar(255) NOT NULL,
  `code` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `id_organisation` varchar(255) DEFAULT NULL,
  `id_categorie_test` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_mesure`),
  KEY `FK3fuvp0eexklmuhitv8q31mw8` (`id_categorie_test`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `t_modelevehicule`
--

DROP TABLE IF EXISTS `t_modelevehicule`;
CREATE TABLE IF NOT EXISTS `t_modelevehicule` (
  `id_modele` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `id_oganisation` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_modele`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `t_operationdecaisse`
--

DROP TABLE IF EXISTS `t_operationdecaisse`;
CREATE TABLE IF NOT EXISTS `t_operationdecaisse` (
  `operation_de_caisse_id` bigint(20) NOT NULL,
  `libelle` varchar(255) DEFAULT NULL,
  `montant` double NOT NULL,
  `numero_ticket` varchar(255) DEFAULT NULL,
  `id_caissier_caisse` varchar(255) DEFAULT NULL,
  `id_session_caisse` bigint(20) DEFAULT NULL,
  `id_taxe` bigint(20) DEFAULT NULL,
  `vente_id_vente` bigint(20) DEFAULT NULL,
  `type` bit(1) NOT NULL,
  `active_status` tinyint(1) DEFAULT '1',
  `created_date` datetime DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `session_caisse_session_caisse_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`operation_de_caisse_id`),
  KEY `FKlo9t5yoyoh4gkfy1mr9e0qs2l` (`id_caissier_caisse`),
  KEY `FKf7u2s3ms0acpq1wero7nwm5ko` (`id_session_caisse`),
  KEY `FKrrmst1cbba9sr20kn2q7wu8qy` (`id_taxe`),
  KEY `FK5hcvb36swy6d137cqwfx0sm1d` (`vente_id_vente`),
  KEY `FK1ymdrmjd0cvf2ik7w2sd2ks1b` (`session_caisse_session_caisse_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `t_operationdecaisse`
--

INSERT INTO `t_operationdecaisse` (`operation_de_caisse_id`, `libelle`, `montant`, `numero_ticket`, `id_caissier_caisse`, `id_session_caisse`, `id_taxe`, `vente_id_vente`, `type`, `active_status`, `created_date`, `modified_date`, `session_caisse_session_caisse_id`) VALUES
(623, NULL, 4100, 'T20201121105835755', NULL, NULL, NULL, 619, b'1', 0, '2020-11-21 09:58:36', '2020-11-21 09:58:36', 1),
(632, NULL, 8000, 'T20201121110303427', NULL, NULL, NULL, 628, b'1', 0, '2020-11-21 10:03:03', '2020-11-21 10:03:03', 625),
(662, NULL, 10000, 'T20201124100509480', NULL, NULL, NULL, 658, b'1', 0, '2020-11-24 09:05:09', '2020-11-24 09:05:09', 651),
(671, NULL, 8000, 'T20201124102129332', NULL, NULL, NULL, 667, b'1', 0, '2020-11-24 09:21:29', '2020-11-24 09:21:29', 656),
(777, NULL, 19080, 'T20201125175402563', NULL, NULL, NULL, 770, b'1', 0, '2020-11-25 16:54:03', '2020-11-25 16:54:03', 651),
(783, NULL, 4889.25, 'T20201125184623080', NULL, NULL, NULL, 779, b'1', 0, '2020-11-25 17:46:23', '2020-11-25 17:46:23', 651);

-- --------------------------------------------------------

--
-- Structure de la table `t_organisation`
--

DROP TABLE IF EXISTS `t_organisation`;
CREATE TABLE IF NOT EXISTS `t_organisation` (
  `organisation_id` bigint(20) NOT NULL,
  `id_organisation_parent` varchar(255) DEFAULT NULL,
  `numero_de_contribuable` varchar(255) DEFAULT NULL,
  `patente` varchar(255) DEFAULT NULL,
  `statut_jurique` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`organisation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `t_organisation`
--

INSERT INTO `t_organisation` (`organisation_id`, `id_organisation_parent`, `numero_de_contribuable`, `patente`, `statut_jurique`) VALUES
(0, 'ras', 'ras', 'ras', 'ras'),
(1, '1', 'ras', 'ras', 'ras');

-- --------------------------------------------------------

--
-- Structure de la table `t_partenaire`
--

DROP TABLE IF EXISTS `t_partenaire`;
CREATE TABLE IF NOT EXISTS `t_partenaire` (
  `partenaire_id` bigint(20) NOT NULL,
  `cni` varchar(255) DEFAULT NULL,
  `date_naiss` datetime DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `lieu_de_naiss` varchar(255) DEFAULT NULL,
  `nom` varchar(255) NOT NULL,
  `passport` varchar(255) DEFAULT NULL,
  `permi_de_conduire` varchar(255) DEFAULT NULL,
  `prenom` varchar(255) DEFAULT NULL,
  `telephone` varchar(255) DEFAULT NULL,
  `organisation_id` bigint(20) NOT NULL,
  PRIMARY KEY (`partenaire_id`),
  UNIQUE KEY `UK_9qhvkc2v1agboguttgi0okha2` (`cni`),
  UNIQUE KEY `UK_i8pjcjxhuk6c3ngo0xgl553ab` (`email`),
  UNIQUE KEY `UK_g4uv6tboby35o1jmh5bk2sx1k` (`passport`),
  UNIQUE KEY `UK_sjon1sjuofa3vlc7k8wdcxqpn` (`permi_de_conduire`),
  UNIQUE KEY `UK_9hekipqemoyshjv98nul4eeox` (`telephone`),
  KEY `FKrx3whru1u46e6yxvq4t148sbm` (`organisation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `t_partenaire`
--

INSERT INTO `t_partenaire` (`partenaire_id`, `cni`, `date_naiss`, `email`, `lieu_de_naiss`, `nom`, `passport`, `permi_de_conduire`, `prenom`, `telephone`, `organisation_id`) VALUES
(0, NULL, NULL, NULL, NULL, 'inexistant', NULL, NULL, '', NULL, 1),
(1, '1235125', NULL, NULL, NULL, 'Tchikou', NULL, NULL, '', NULL, 1),
(39, '1234566520', '2020-11-16 23:00:00', NULL, NULL, 'proprio', '', NULL, 'jules', '652525252', 0),
(47, '0002233225', NULL, NULL, NULL, 'proprio2', NULL, NULL, 'jules', '655885518', 0),
(124, '502325504', NULL, NULL, NULL, 'kalab', NULL, NULL, 'Cit', '562689622', 0),
(654, '0123456789', '1999-11-09 23:00:00', NULL, NULL, 'TATA', NULL, NULL, 'TEBOH', '999888777', 0),
(665, '126665462', NULL, NULL, NULL, 'NCHIA  MVO', NULL, NULL, 'ANDREW', '698522355', 0);

-- --------------------------------------------------------

--
-- Structure de la table `t_pays`
--

DROP TABLE IF EXISTS `t_pays`;
CREATE TABLE IF NOT EXISTS `t_pays` (
  `pays_id` bigint(20) NOT NULL,
  `nom_pays` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `active_status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`pays_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `t_pays`
--

INSERT INTO `t_pays` (`pays_id`, `nom_pays`, `description`, `created_date`, `modified_date`, `active_status`) VALUES
(15, 'Tchad', NULL, '2020-11-12 13:44:32', '2020-11-12 13:44:32', NULL),
(16, 'Cameroun', NULL, '2020-11-12 13:44:43', '2020-11-12 13:44:43', NULL);

-- --------------------------------------------------------

--
-- Structure de la table `t_posales`
--

DROP TABLE IF EXISTS `t_posales`;
CREATE TABLE IF NOT EXISTS `t_posales` (
  `posales_id` bigint(20) NOT NULL,
  `reference` varchar(255) NOT NULL,
  `status` bit(1) NOT NULL,
  `hold_hold_id` bigint(20) DEFAULT NULL,
  `produit_produit_id` bigint(20) DEFAULT NULL,
  `session_caisse_session_caisse_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`posales_id`),
  UNIQUE KEY `UK_gehddo0ucet22cgcxncmgbhrk` (`reference`),
  KEY `FKjtfp5f8gnjdxd60mpr1tpjuvd` (`hold_hold_id`),
  KEY `FK5yy8v1wldurfmxi7qb02nsmjn` (`produit_produit_id`),
  KEY `FK33yjr113jej8nucwwx988ynq1` (`session_caisse_session_caisse_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `t_posales`
--

INSERT INTO `t_posales` (`posales_id`, `reference`, `status`, `hold_hold_id`, `produit_produit_id`, `session_caisse_session_caisse_id`) VALUES
(664, 'LT642JA', b'0', 657, 3, 656);

-- --------------------------------------------------------

--
-- Structure de la table `t_produit`
--

DROP TABLE IF EXISTS `t_produit`;
CREATE TABLE IF NOT EXISTS `t_produit` (
  `produit_id` bigint(20) NOT NULL,
  `delai_validite` int(11) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `img` varchar(255) DEFAULT NULL,
  `libelle` varchar(255) DEFAULT NULL,
  `prix` double NOT NULL,
  `categorie_produit_id` bigint(20) DEFAULT NULL,
  `active_status` tinyint(1) DEFAULT '1',
  `created_date` datetime DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  PRIMARY KEY (`produit_id`),
  KEY `FK6x2nd4ad7a5po0x2nj7ytamts` (`categorie_produit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `t_produit`
--

INSERT INTO `t_produit` (`produit_id`, `delai_validite`, `description`, `img`, `libelle`, `prix`, `categorie_produit_id`, `active_status`, `created_date`, `modified_date`) VALUES
(0, 0, 'Contre visite', '/images/cv.jpg', 'cv', 0, 1, 1, NULL, NULL),
(1, 0, 'Décaissement', '/images/dec.png', 'dec', 0, 17, 1, NULL, NULL),
(2, 3, 'Taxi et véhicule auto-école', '/images/catA.jpg', 'catA', 4100, 1, 1, NULL, NULL),
(3, 12, 'Véhicule personnel', '/images/catB.jpg', 'catB', 8000, 1, 1, NULL, NULL),
(4, 12, 'pickup personnel', '/images/catB1.jpg', 'catB1', 8000, 1, 1, NULL, NULL),
(5, 12, 'bus > 3500 kg', '/images/catCPL.jpg', 'catCPL', 8000, 1, 1, NULL, NULL),
(6, 12, 'Bus < 3500 kg', '/images/catCVL.jfif', 'catCVL', 10000, 1, 1, NULL, NULL),
(7, 12, 'Remorque , semi remorque :', '/images/catDPL.jpg', 'catDPL', 12000, 1, 1, NULL, NULL);

-- --------------------------------------------------------

--
-- Structure de la table `t_proprietairevehicule`
--

DROP TABLE IF EXISTS `t_proprietairevehicule`;
CREATE TABLE IF NOT EXISTS `t_proprietairevehicule` (
  `proprietaire_vehicule_id` bigint(20) NOT NULL,
  `active_status` tinyint(1) DEFAULT '1',
  `created_date` datetime DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `id_organisation` varchar(255) DEFAULT NULL,
  `partenaire_partenaire_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`proprietaire_vehicule_id`),
  KEY `FKe90vcjwiylp6d1x1gmmgkw8ph` (`partenaire_partenaire_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `t_proprietairevehicule`
--

INSERT INTO `t_proprietairevehicule` (`proprietaire_vehicule_id`, `active_status`, `created_date`, `modified_date`, `description`, `id_organisation`, `partenaire_partenaire_id`) VALUES
(1, 1, '2020-11-16 00:00:00', '2020-11-16 00:00:00', 'rien', NULL, 1),
(40, 0, '2020-11-17 09:39:41', '2020-11-17 09:39:41', NULL, NULL, 39),
(48, 0, '2020-11-17 10:04:34', '2020-11-17 10:04:34', NULL, NULL, 47);

-- --------------------------------------------------------

--
-- Structure de la table `t_sessioncaisse`
--

DROP TABLE IF EXISTS `t_sessioncaisse`;
CREATE TABLE IF NOT EXISTS `t_sessioncaisse` (
  `session_caisse_id` bigint(20) NOT NULL,
  `active` bit(1) NOT NULL,
  `date_heure_fermeture` datetime DEFAULT NULL,
  `date_heure_ouverture` datetime DEFAULT NULL,
  `montant_ouverture` double NOT NULL,
  `montantfermeture` double NOT NULL,
  `organisation_id_organisation_id` bigint(20) DEFAULT NULL,
  `user_utilisateur_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`session_caisse_id`),
  KEY `FK6enmdevjv374hptdgtl5b3yfd` (`organisation_id_organisation_id`),
  KEY `FKon8ieshhijsnjtyj36wp800v2` (`user_utilisateur_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `t_sessioncaisse`
--

INSERT INTO `t_sessioncaisse` (`session_caisse_id`, `active`, `date_heure_fermeture`, `date_heure_ouverture`, `montant_ouverture`, `montantfermeture`, `organisation_id_organisation_id`, `user_utilisateur_id`) VALUES
(1, b'0', '2020-11-16 15:31:22', NULL, 9000, 100, 1, 1),
(31, b'0', '2020-11-17 08:59:35', '2020-11-17 08:58:07', 0, 0, 1, 1),
(33, b'0', '2020-11-17 08:59:05', '2020-11-17 08:58:40', 50000, 50000, 1, 1),
(83, b'0', '2020-11-20 08:45:11', '2020-11-19 09:40:25', 120, 0, 1, 1),
(91, b'0', '2020-11-20 10:50:14', '2020-11-20 08:46:09', 12000, 2, 1, 1),
(129, b'0', '2020-11-20 11:01:32', '2020-11-20 10:51:21', 3, 0, 1, 1),
(134, b'0', '2020-11-21 08:16:24', '2020-11-20 11:01:54', 0, 0, 1, 1),
(185, b'0', '2020-11-21 09:54:36', '2020-11-20 11:30:35', 1000, 0, 1, 1),
(616, b'0', '2020-11-21 09:58:50', '2020-11-21 09:58:15', 10000, 10000, 1, 1),
(625, b'0', '2020-11-21 10:03:24', '2020-11-21 10:02:44', 0, 0, 1, 1),
(642, b'0', '2020-11-24 08:26:07', '2020-11-23 09:22:00', 0, 0, 1, 1),
(651, b'1', NULL, '2020-11-24 08:26:37', 0, 0, 1, 1),
(656, b'1', NULL, '2020-11-24 09:02:22', 0, 0, 1, 1);

-- --------------------------------------------------------

--
-- Structure de la table `t_statut_code`
--

DROP TABLE IF EXISTS `t_statut_code`;
CREATE TABLE IF NOT EXISTS `t_statut_code` (
  `id` bigint(20) NOT NULL,
  `libelle` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `t_taxe`
--

DROP TABLE IF EXISTS `t_taxe`;
CREATE TABLE IF NOT EXISTS `t_taxe` (
  `taxe_id` bigint(20) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `incluse` bit(1) NOT NULL,
  `nom` varchar(255) DEFAULT NULL,
  `valeur` double NOT NULL,
  PRIMARY KEY (`taxe_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `t_taxe`
--

INSERT INTO `t_taxe` (`taxe_id`, `description`, `incluse`, `nom`, `valeur`) VALUES
(1, 'Taxe sur la valeur ajoutée', b'0', 'TVA', 19.25);

-- --------------------------------------------------------

--
-- Structure de la table `t_taxeproduit`
--

DROP TABLE IF EXISTS `t_taxeproduit`;
CREATE TABLE IF NOT EXISTS `t_taxeproduit` (
  `taxe_produit_id` bigint(20) NOT NULL,
  `produit_produit_id` bigint(20) DEFAULT NULL,
  `taxe_taxe_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`taxe_produit_id`),
  KEY `FKhucfbgtio5ps1jfvxol53chkh` (`produit_produit_id`),
  KEY `FKtm8pkm8sbfctol51qb1kyrx8` (`taxe_taxe_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `t_taxeproduit`
--

INSERT INTO `t_taxeproduit` (`taxe_produit_id`, `produit_produit_id`, `taxe_taxe_id`) VALUES
(1, 6, 1),
(2, 2, 1),
(3, 3, 1),
(4, 4, 1),
(5, 5, 1),
(6, 7, 1);

-- --------------------------------------------------------

--
-- Structure de la table `t_utilisateur`
--

DROP TABLE IF EXISTS `t_utilisateur`;
CREATE TABLE IF NOT EXISTS `t_utilisateur` (
  `utilisateur_id` bigint(20) NOT NULL,
  `id_organisation` varchar(255) DEFAULT NULL,
  `login` varchar(255) DEFAULT NULL,
  `mot_de_passe` varchar(255) DEFAULT NULL,
  `partenaire_partenaire_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`utilisateur_id`),
  KEY `FKhdm5d2oau3y995sbpwheybhur` (`partenaire_partenaire_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `t_utilisateur`
--

INSERT INTO `t_utilisateur` (`utilisateur_id`, `id_organisation`, `login`, `mot_de_passe`, `partenaire_partenaire_id`) VALUES
(1, NULL, 'tchoko', 'tchoko', 1);

-- --------------------------------------------------------

--
-- Structure de la table `t_valeurtest`
--

DROP TABLE IF EXISTS `t_valeurtest`;
CREATE TABLE IF NOT EXISTS `t_valeurtest` (
  `uuid` binary(255) NOT NULL,
  `code` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `id_organisation` varchar(255) DEFAULT NULL,
  `valeur` double NOT NULL,
  `id_machine` varchar(255) DEFAULT NULL,
  `id_mesure` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`uuid`),
  KEY `FK8yqyj2nu0e780o1or47xhgp9n` (`id_machine`),
  KEY `FKc8rn65uirgctxynbh1imu30v9` (`id_mesure`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `t_vehicule`
--

DROP TABLE IF EXISTS `t_vehicule`;
CREATE TABLE IF NOT EXISTS `t_vehicule` (
  `vehicule_id` bigint(20) NOT NULL,
  `active_status` tinyint(1) DEFAULT '1',
  `created_date` datetime DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `carrosserie` varchar(255) DEFAULT NULL,
  `charge_utile` int(11) NOT NULL,
  `chassis` varchar(255) DEFAULT NULL,
  `cylindre` int(11) NOT NULL,
  `date_mise_en_circulation` datetime DEFAULT NULL,
  `place_assise` int(11) NOT NULL,
  `poids_total_cha` int(11) NOT NULL,
  `poids_vide` int(11) NOT NULL,
  `premiere_mise_en_circulation` datetime DEFAULT NULL,
  `puiss_admin` int(11) NOT NULL,
  `type_vehicule` varchar(255) DEFAULT NULL,
  `energie_energie_id` bigint(20) DEFAULT NULL,
  `marque_vehicule_marque_vehicule_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`vehicule_id`),
  KEY `FKj38ug51jnx9wstsafvaudhdme` (`energie_energie_id`),
  KEY `FK6pjbufl3y0piedubc4akf5uky` (`marque_vehicule_marque_vehicule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `t_vehicule`
--

INSERT INTO `t_vehicule` (`vehicule_id`, `active_status`, `created_date`, `modified_date`, `carrosserie`, `charge_utile`, `chassis`, `cylindre`, `date_mise_en_circulation`, `place_assise`, `poids_total_cha`, `poids_vide`, `premiere_mise_en_circulation`, `puiss_admin`, `type_vehicule`, `energie_energie_id`, `marque_vehicule_marque_vehicule_id`) VALUES
(51, 0, '2020-11-17 13:02:30', '2020-11-17 13:02:30', 'MOS', 1111, '', 0, NULL, 5, 45000, 150, '2020-11-02 00:00:00', 52, '21AQ', 1, 2),
(54, 1, '2020-11-17 16:03:24', '2020-11-17 16:03:24', 'MO', 12000, 'ch0001548', 0, NULL, 5, 540, 15222, '2020-11-07 00:00:00', 52, '21AQA', 2, 2),
(55, 0, '2020-11-17 16:07:31', '2020-11-17 16:07:31', 'MO', 1200, '5678', 0, NULL, 4, 5200, 5422, '2020-11-13 00:00:00', 52, '', 2, 2),
(76, 0, '2020-11-18 15:18:08', '2020-11-18 15:18:08', 'MOU', 6000, '12356', 56, NULL, 5, 12000, 6000, '2020-11-28 00:00:00', 52, '21AQ', 2, 2),
(634, 0, '2020-11-21 13:26:43', '2020-11-21 13:26:43', 'MO', 1200, NULL, 0, NULL, 4, 5200, 5422, NULL, 52, '1254', 2, 2),
(635, 0, '2020-11-21 13:26:43', '2020-11-21 13:26:43', 'MO', 1200, NULL, 0, NULL, 4, 5200, 5422, NULL, 52, '1254', 2, 2),
(636, 0, '2020-11-21 13:32:47', '2020-11-21 13:32:47', 'MO', 1200, NULL, 0, NULL, 4, 5200, 5422, NULL, 52, '21AQA', 2, 2),
(637, 0, '2020-11-21 13:32:47', '2020-11-21 13:32:47', 'MO', 1200, NULL, 0, NULL, 4, 5200, 5422, NULL, 52, '21AQA', 2, 2),
(638, 0, '2020-11-21 13:35:21', '2020-11-21 13:35:21', 'MO', 1200, NULL, 0, NULL, 4, 5200, 5422, NULL, 52, '21AQA', 2, 2),
(639, 0, '2020-11-21 13:35:21', '2020-11-21 13:35:21', 'MO', 1200, NULL, 0, NULL, 4, 5200, 5422, NULL, 52, '21AQA', 2, 2),
(640, 0, '2020-11-21 13:38:37', '2020-11-21 13:38:37', 'MO', 1200, NULL, 0, NULL, 4, 5200, 5422, NULL, 0, '21AQA', 2, 2),
(641, 0, '2020-11-21 13:38:38', '2020-11-21 13:38:38', 'MO', 1200, NULL, 0, NULL, 4, 5200, 5422, NULL, 0, '21AQA', 2, 2);

-- --------------------------------------------------------

--
-- Structure de la table `t_vendeur`
--

DROP TABLE IF EXISTS `t_vendeur`;
CREATE TABLE IF NOT EXISTS `t_vendeur` (
  `vendeur_id` bigint(20) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `partenaire_partenaire_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`vendeur_id`),
  KEY `FKt3t02gp6gxwqtskqm6ehiqwo8` (`partenaire_partenaire_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `t_vendeur`
--

INSERT INTO `t_vendeur` (`vendeur_id`, `description`, `partenaire_partenaire_id`) VALUES
(0, NULL, 0);

-- --------------------------------------------------------

--
-- Structure de la table `t_vente`
--

DROP TABLE IF EXISTS `t_vente`;
CREATE TABLE IF NOT EXISTS `t_vente` (
  `id_vente` bigint(20) NOT NULL,
  `montantht` double NOT NULL,
  `montant_total` double NOT NULL,
  `num_facture` varchar(255) DEFAULT NULL,
  `client_client_id` bigint(20) DEFAULT NULL,
  `contact_contact_id` bigint(20) DEFAULT NULL,
  `session_caisse_session_caisse_id` bigint(20) DEFAULT NULL,
  `vendeur_vendeur_id` bigint(20) DEFAULT NULL,
  `visite_id_visite` bigint(20) DEFAULT NULL,
  `active_status` tinyint(1) DEFAULT '1',
  `created_date` datetime DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `statut` int(11) NOT NULL,
  PRIMARY KEY (`id_vente`),
  KEY `FKg3bsptyyc6el4r9kluycb0ceq` (`client_client_id`),
  KEY `FK2ig0spxlrxgeisti5tuf543g5` (`contact_contact_id`),
  KEY `FKpj7yvjg643sjxxn420pwliv1p` (`session_caisse_session_caisse_id`),
  KEY `FKglemp0qqso8c6v9ch276fvkt7` (`vendeur_vendeur_id`),
  KEY `FK8r70sh7n7dht5ok2pqnct4r8k` (`visite_id_visite`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `t_vente`
--

INSERT INTO `t_vente` (`id_vente`, `montantht`, `montant_total`, `num_facture`, `client_client_id`, `contact_contact_id`, `session_caisse_session_caisse_id`, `vendeur_vendeur_id`, `visite_id_visite`, `active_status`, `created_date`, `modified_date`, `statut`) VALUES
(1, 0, 0, '0', 1, 0, NULL, 0, 1, 1, '2020-11-13 00:00:00', NULL, 0),
(3, 3000, 30000, NULL, 1, 0, 1, 0, 2, 1, NULL, NULL, 1),
(25, 29450, 30000, 'F20201116101548807', 1, 0, NULL, 0, NULL, 0, '2020-11-16 09:15:49', '2020-11-16 09:15:49', 0),
(143, 20000, 20000, 'F20201120121247423', NULL, 0, 134, 0, NULL, 0, '2020-11-20 11:12:48', '2020-11-20 11:12:48', 0),
(154, 8200, 8200, 'F20201120121725594', NULL, 0, 1, 0, NULL, 0, '2020-11-20 11:17:26', '2020-11-20 11:17:26', 0),
(162, 22000, 22000, 'F20201120122054000', NULL, 0, 134, 0, NULL, 0, '2020-11-20 11:20:54', '2020-11-20 11:20:54', 0),
(167, 16100, 16100, 'F20201120122454476', NULL, 0, 1, 0, NULL, 0, '2020-11-20 11:24:54', '2020-11-20 11:24:54', 0),
(177, 16100, 16100, 'F20201120122919494', NULL, 0, 134, 0, NULL, 0, '2020-11-20 11:29:19', '2020-11-20 11:29:19', 0),
(199, 22000, 22000, 'F20201120130300124', NULL, 0, 1, 0, NULL, 0, '2020-11-20 12:03:00', '2020-11-20 12:03:00', 0),
(209, 12100, 12100, 'F20201120130835479', NULL, 0, 134, 0, NULL, 0, '2020-11-20 12:08:35', '2020-11-20 12:08:35', 0),
(220, 12100, 12100, 'F20201120131908087', NULL, 0, 134, 0, NULL, 0, '2020-11-20 12:19:08', '2020-11-20 12:19:08', 0),
(230, 12100, 12100, 'F20201120132221339', NULL, 0, 134, 0, NULL, 0, '2020-11-20 12:22:21', '2020-11-20 12:22:21', 0),
(238, 12100, 12100, 'F20201120132242389', NULL, 0, 134, 0, NULL, 0, '2020-11-20 12:22:42', '2020-11-20 12:22:42', 0),
(248, 16000, 16000, 'F20201120132350949', NULL, 0, 134, 0, NULL, 0, '2020-11-20 12:23:51', '2020-11-20 12:23:51', 0),
(264, 16000, 16000, 'F20201120132545915', NULL, 0, 134, 0, NULL, 0, '2020-11-20 12:25:46', '2020-11-20 12:25:46', 0),
(274, 12100, 12100, 'F20201120132838836', NULL, 0, 134, 0, NULL, 0, '2020-11-20 12:28:39', '2020-11-20 12:28:39', 0),
(286, 20000, 20000, 'F20201120133519113', NULL, 0, 1, 0, NULL, 0, '2020-11-20 12:35:19', '2020-11-20 12:35:19', 0),
(302, 12100, 12100, 'F20201120133841658', NULL, 0, 1, 0, NULL, 0, '2020-11-20 12:38:42', '2020-11-20 12:38:42', 0),
(312, 4100, 4100, 'F20201120135047624', NULL, 0, 1, 0, NULL, 0, '2020-11-20 12:50:48', '2020-11-20 12:50:48', 0),
(321, 4100, 4100, 'F20201120135139743', NULL, 0, 1, 0, NULL, 0, '2020-11-20 12:51:40', '2020-11-20 12:51:40', 0),
(327, 4100, 4100, 'F20201120135547590', NULL, 125, 1, 0, NULL, 0, '2020-11-20 12:55:48', '2020-11-20 12:55:48', 0),
(333, 4100, 4100, 'F20201120135733331', NULL, 125, 1, 0, NULL, 0, '2020-11-20 12:57:33', '2020-11-20 12:57:33', 0),
(339, 4100, 4100, 'F20201120135834543', NULL, 125, 1, 0, NULL, 0, '2020-11-20 12:58:35', '2020-11-20 12:58:35', 0),
(352, 16000, 16000, 'F20201120142047462', NULL, 125, 1, 0, NULL, 0, '2020-11-20 13:20:47', '2020-11-20 13:20:47', 0),
(446, 4100, 4100, 'F20201120150135351', NULL, 125, 1, 0, NULL, 0, '2020-11-20 14:01:35', '2020-11-20 14:01:35', 0),
(485, 4100, 4100, 'F20201120151551268', NULL, 125, 1, 0, NULL, 0, '2020-11-20 14:15:51', '2020-11-20 14:15:51', 0),
(491, 4100, 4100, 'F20201120151640034', NULL, 125, 1, 0, NULL, 0, '2020-11-20 14:16:40', '2020-11-20 14:16:40', 0),
(500, 4100, 4100, 'F20201120155558091', NULL, 125, 1, 0, NULL, 0, '2020-11-20 14:55:58', '2020-11-20 14:55:58', 0),
(513, 8200, 8200, 'F20201120162232550', NULL, 125, 1, 0, NULL, 0, '2020-11-20 15:22:33', '2020-11-20 15:22:33', 0),
(523, 4100, 4100, 'F20201120162518972', NULL, 125, 1, 0, NULL, 0, '2020-11-20 15:25:19', '2020-11-20 15:25:19', 0),
(531, 8000, 8000, 'F20201120163756565', NULL, 125, 1, 0, NULL, 0, '2020-11-20 15:37:57', '2020-11-20 15:37:57', 0),
(537, 4100, 4100, 'F20201120163853550', NULL, 125, 1, 0, NULL, 0, '2020-11-20 15:38:54', '2020-11-20 15:38:54', 0),
(544, 8200, 8200, 'F20201120163941206', NULL, 125, 1, 0, NULL, 0, '2020-11-20 15:39:41', '2020-11-20 15:39:41', 0),
(553, 4100, 4100, 'F20201120164212406', NULL, 125, 1, 0, NULL, 0, '2020-11-20 15:42:12', '2020-11-20 15:42:12', 0),
(561, 4100, 4100, 'F20201120165637924', NULL, 125, 1, 0, NULL, 0, '2020-11-20 15:56:38', '2020-11-20 15:56:38', 0),
(567, 4100, 4100, 'F20201120165913786', NULL, 125, 1, 0, NULL, 0, '2020-11-20 15:59:14', '2020-11-20 15:59:14', 0),
(575, 8000, 8000, 'F20201120170024110', NULL, 125, 1, 0, NULL, 0, '2020-11-20 16:00:24', '2020-11-20 16:00:24', 0),
(582, 8200, 8200, 'F20201120170123711', NULL, 125, 1, 0, NULL, 0, '2020-11-20 16:01:24', '2020-11-20 16:01:24', 0),
(592, 8000, 8000, 'F20201120170154073', NULL, 125, 1, 0, NULL, 0, '2020-11-20 16:01:54', '2020-11-20 16:01:54', 0),
(601, 8000, 8000, 'F20201121103125830', NULL, 125, 1, 0, NULL, 0, '2020-11-21 09:31:26', '2020-11-21 09:31:26', 0),
(607, 16000, 16000, 'F20201121105303836', NULL, 125, 1, 0, NULL, 0, '2020-11-21 09:53:04', '2020-11-21 09:53:04', 0),
(619, 4100, 4100, 'F20201121105835582', NULL, 125, 1, 0, NULL, 0, '2020-11-21 09:58:36', '2020-11-21 09:58:36', 0),
(628, 8000, 8000, 'F20201121110303209', NULL, 125, 625, 0, NULL, 0, '2020-11-21 10:03:03', '2020-11-21 10:03:03', 0),
(658, 10000, 10000, 'F20201124100509176', NULL, 655, 651, 0, NULL, 0, '2020-11-24 09:05:09', '2020-11-24 09:05:09', 0),
(667, 8000, 8000, 'F20201124102129003', NULL, 666, 656, 0, NULL, 0, '2020-11-24 09:21:29', '2020-11-24 09:21:29', 0),
(682, 0, 0, 'F20201124115206067', NULL, 125, 651, 0, NULL, 0, '2020-11-24 10:52:06', '2020-11-24 10:52:06', 0),
(688, 0, 0, 'F20201124125300366', NULL, 655, 651, 0, NULL, 0, '2020-11-24 11:53:00', '2020-11-24 11:53:00', 0),
(770, 16000, 19080, 'F20201125175400782', NULL, 125, 651, 0, NULL, 0, '2020-11-25 16:54:02', '2020-11-25 16:54:02', 0),
(779, 4100, 4889.25, 'F20201125184622880', NULL, 125, 651, 0, NULL, 0, '2020-11-25 17:46:23', '2020-11-25 17:46:23', 0);

-- --------------------------------------------------------

--
-- Structure de la table `t_visite`
--

DROP TABLE IF EXISTS `t_visite`;
CREATE TABLE IF NOT EXISTS `t_visite` (
  `id_visite` bigint(20) NOT NULL,
  `contre_visite` bit(1) NOT NULL,
  `date_debut` datetime DEFAULT NULL,
  `date_fin` datetime DEFAULT NULL,
  `encours` bit(1) NOT NULL,
  `id_organisation` varchar(255) DEFAULT NULL,
  `statut` int(11) NOT NULL,
  `id_caissier` varchar(255) DEFAULT NULL,
  `carte_grise_carte_grise_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id_visite`),
  KEY `FKsqur57f1d62twreq225a4fa9a` (`id_caissier`),
  KEY `FK4l0tfdjj1bfgmpomfiqatvdjv` (`carte_grise_carte_grise_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `t_visite`
--

INSERT INTO `t_visite` (`id_visite`, `contre_visite`, `date_debut`, `date_fin`, `encours`, `id_organisation`, `statut`, `id_caissier`, `carte_grise_carte_grise_id`) VALUES
(1, b'0', '2020-11-09 00:00:00', '2020-11-18 16:03:45', b'1', '1', 0, '1', 52),
(2, b'0', '2020-11-17 00:00:00', '2020-11-18 16:03:35', b'1', '1', 2, '1', 56),
(144, b'0', '2020-11-20 11:12:48', NULL, b'1', NULL, 0, NULL, 146),
(659, b'0', '2020-11-24 09:05:09', NULL, b'1', NULL, 0, NULL, 661),
(668, b'0', '2020-11-24 09:21:29', '2020-11-25 00:00:00', b'0', NULL, 0, NULL, 670),
(683, b'0', '2020-11-24 10:52:06', NULL, b'1', NULL, 8, NULL, 685),
(771, b'0', '2020-11-25 16:54:02', NULL, b'1', NULL, 0, NULL, 773),
(774, b'0', '2020-11-25 16:54:02', NULL, b'1', NULL, 0, NULL, 776),
(780, b'0', '2020-11-25 17:46:23', NULL, b'1', NULL, 0, NULL, 782);

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `t_adresse`
--
ALTER TABLE `t_adresse`
  ADD CONSTRAINT `FKbj6co7futohp2f73tv8rd9p0t` FOREIGN KEY (`organisation_organisation_id`) REFERENCES `t_organisation` (`organisation_id`),
  ADD CONSTRAINT `FKen7rgd80o52vt6qh5n0boq533` FOREIGN KEY (`division_pays_division_pays_id`) REFERENCES `t_divisionpays` (`division_pays_id`),
  ADD CONSTRAINT `FKetf7cxpx9xuyfd8urkcpfhcb2` FOREIGN KEY (`partenaire_partenaire_id`) REFERENCES `t_partenaire` (`partenaire_id`),
  ADD CONSTRAINT `FKmw2hcqcfx40tk1vf70aen62i6` FOREIGN KEY (`pays_pays_id`) REFERENCES `t_pays` (`pays_id`);

--
-- Contraintes pour la table `t_caissier`
--
ALTER TABLE `t_caissier`
  ADD CONSTRAINT `FK1yxnow4g3apqsp77qv45u18c6` FOREIGN KEY (`id_utilisateur`) REFERENCES `t_utilisateur` (`utilisateur_id`),
  ADD CONSTRAINT `FKb5vs21xqb41toy45rerufeiqn` FOREIGN KEY (`partenaire_partenaire_id`) REFERENCES `t_partenaire` (`partenaire_id`);

--
-- Contraintes pour la table `t_caissiercaisse`
--
ALTER TABLE `t_caissiercaisse`
  ADD CONSTRAINT `FK7g63caai3y82w4ib88f4d355t` FOREIGN KEY (`caisse_id`) REFERENCES `t_caisse` (`caisse_id`),
  ADD CONSTRAINT `FKigub17h2wha6t91kx8escg301` FOREIGN KEY (`caissier_id`) REFERENCES `t_caissier` (`caissier_id`);

--
-- Contraintes pour la table `t_cartegrise`
--
ALTER TABLE `t_cartegrise`
  ADD CONSTRAINT `FK26x8hvnfltyke14adj6xfgj1a` FOREIGN KEY (`vehicule_vehicule_id`) REFERENCES `t_vehicule` (`vehicule_id`),
  ADD CONSTRAINT `FKn1yfvlchuge0up4subrluej11` FOREIGN KEY (`produit_produit_id`) REFERENCES `t_produit` (`produit_id`),
  ADD CONSTRAINT `FKngt91bigrgp300yjgkpihfvvu` FOREIGN KEY (`proprietaire_vehicule_proprietaire_vehicule_id`) REFERENCES `t_proprietairevehicule` (`proprietaire_vehicule_id`);

--
-- Contraintes pour la table `t_categorietestmachine`
--
ALTER TABLE `t_categorietestmachine`
  ADD CONSTRAINT `FK2u2k6qto5o0vy70lisb3bxqqs` FOREIGN KEY (`idcategorietest`) REFERENCES `t_categorietest` (`id_categorie_test`),
  ADD CONSTRAINT `FKct34i49dnwqouievc6t3uoiak` FOREIGN KEY (`id_machine`) REFERENCES `t_machine` (`id_machine`);

--
-- Contraintes pour la table `t_client`
--
ALTER TABLE `t_client`
  ADD CONSTRAINT `FK2ckb50s00267ade3ukshgbmy9` FOREIGN KEY (`partenaire_partenaire_id`) REFERENCES `t_partenaire` (`partenaire_id`);

--
-- Contraintes pour la table `t_contact`
--
ALTER TABLE `t_contact`
  ADD CONSTRAINT `FK2uqfos85e0qp7gstaanr7rwts` FOREIGN KEY (`client_client_id`) REFERENCES `t_client` (`client_id`),
  ADD CONSTRAINT `FKr81l195f851wgays13e165dkk` FOREIGN KEY (`partenaire_partenaire_id`) REFERENCES `t_partenaire` (`partenaire_id`);

--
-- Contraintes pour la table `t_controleur`
--
ALTER TABLE `t_controleur`
  ADD CONSTRAINT `FKgmpbobpbsfqerix60g5infpx1` FOREIGN KEY (`partenaire_partenaire_id`) REFERENCES `t_partenaire` (`partenaire_id`),
  ADD CONSTRAINT `FKo0oml59pvrsxht3co2d40xwr7` FOREIGN KEY (`utilisateur_utilisateur_id`) REFERENCES `t_utilisateur` (`utilisateur_id`);

--
-- Contraintes pour la table `t_detailvente`
--
ALTER TABLE `t_detailvente`
  ADD CONSTRAINT `FKqscesvpcsvhogcxslgdxi64ai` FOREIGN KEY (`vente_id_vente`) REFERENCES `t_vente` (`id_vente`),
  ADD CONSTRAINT `FKsw05awqtjrm9bb8x5j7bo9mv7` FOREIGN KEY (`id_produit`) REFERENCES `t_produit` (`produit_id`);

--
-- Contraintes pour la table `t_divisionpays`
--
ALTER TABLE `t_divisionpays`
  ADD CONSTRAINT `FKba9kogqnj8b8n15s0ctm6xtcq` FOREIGN KEY (`pays_pays_id`) REFERENCES `t_pays` (`pays_id`);

--
-- Contraintes pour la table `t_hold`
--
ALTER TABLE `t_hold`
  ADD CONSTRAINT `FKeoiccvimdi16sscg8ggj0fvk` FOREIGN KEY (`session_caisse_session_caisse_id`) REFERENCES `t_sessioncaisse` (`session_caisse_id`);

--
-- Contraintes pour la table `t_inspection`
--
ALTER TABLE `t_inspection`
  ADD CONSTRAINT `FKdb8d13pyc050eax4h72owwxvx` FOREIGN KEY (`ligne_id_ligne`) REFERENCES `t_ligne` (`id_ligne`),
  ADD CONSTRAINT `FKfc0aql5mi49uy2d8m5talqjvr` FOREIGN KEY (`controleur_id_controleur`) REFERENCES `t_controleur` (`id_controleur`),
  ADD CONSTRAINT `FKo421h7b2s8q85p2htfyfqt1cv` FOREIGN KEY (`visite_id_visite`) REFERENCES `t_visite` (`id_visite`),
  ADD CONSTRAINT `FKqd2x540t65gib4n2whvuuqxh9` FOREIGN KEY (`produit_produit_id`) REFERENCES `t_produit` (`produit_id`);

--
-- Contraintes pour la table `t_lignemachine`
--
ALTER TABLE `t_lignemachine`
  ADD CONSTRAINT `FKehrgix9rl6bov18xohebfpq54` FOREIGN KEY (`id_ligne`) REFERENCES `t_ligne` (`id_ligne`),
  ADD CONSTRAINT `FKempadxvkep051090703xcfp22` FOREIGN KEY (`id_machine`) REFERENCES `t_machine` (`id_machine`);

--
-- Contraintes pour la table `t_mesure`
--
ALTER TABLE `t_mesure`
  ADD CONSTRAINT `FK3fuvp0eexklmuhitv8q31mw8` FOREIGN KEY (`id_categorie_test`) REFERENCES `t_categorietest` (`id_categorie_test`);

--
-- Contraintes pour la table `t_operationdecaisse`
--
ALTER TABLE `t_operationdecaisse`
  ADD CONSTRAINT `FK1ymdrmjd0cvf2ik7w2sd2ks1b` FOREIGN KEY (`session_caisse_session_caisse_id`) REFERENCES `t_sessioncaisse` (`session_caisse_id`),
  ADD CONSTRAINT `FK5hcvb36swy6d137cqwfx0sm1d` FOREIGN KEY (`vente_id_vente`) REFERENCES `t_vente` (`id_vente`),
  ADD CONSTRAINT `FKf7u2s3ms0acpq1wero7nwm5ko` FOREIGN KEY (`id_session_caisse`) REFERENCES `t_sessioncaisse` (`session_caisse_id`),
  ADD CONSTRAINT `FKlo9t5yoyoh4gkfy1mr9e0qs2l` FOREIGN KEY (`id_caissier_caisse`) REFERENCES `t_caissiercaisse` (`caissier_caisse_id`),
  ADD CONSTRAINT `FKrrmst1cbba9sr20kn2q7wu8qy` FOREIGN KEY (`id_taxe`) REFERENCES `t_taxe` (`taxe_id`);

--
-- Contraintes pour la table `t_partenaire`
--
ALTER TABLE `t_partenaire`
  ADD CONSTRAINT `FKrx3whru1u46e6yxvq4t148sbm` FOREIGN KEY (`organisation_id`) REFERENCES `t_organisation` (`organisation_id`);

--
-- Contraintes pour la table `t_posales`
--
ALTER TABLE `t_posales`
  ADD CONSTRAINT `FK33yjr113jej8nucwwx988ynq1` FOREIGN KEY (`session_caisse_session_caisse_id`) REFERENCES `t_sessioncaisse` (`session_caisse_id`),
  ADD CONSTRAINT `FK5yy8v1wldurfmxi7qb02nsmjn` FOREIGN KEY (`produit_produit_id`) REFERENCES `t_produit` (`produit_id`),
  ADD CONSTRAINT `FKjtfp5f8gnjdxd60mpr1tpjuvd` FOREIGN KEY (`hold_hold_id`) REFERENCES `t_hold` (`hold_id`);

--
-- Contraintes pour la table `t_produit`
--
ALTER TABLE `t_produit`
  ADD CONSTRAINT `FK6x2nd4ad7a5po0x2nj7ytamts` FOREIGN KEY (`categorie_produit_id`) REFERENCES `t_categorieproduit` (`categorie_produit_id`);

--
-- Contraintes pour la table `t_proprietairevehicule`
--
ALTER TABLE `t_proprietairevehicule`
  ADD CONSTRAINT `FKe90vcjwiylp6d1x1gmmgkw8ph` FOREIGN KEY (`partenaire_partenaire_id`) REFERENCES `t_partenaire` (`partenaire_id`);

--
-- Contraintes pour la table `t_sessioncaisse`
--
ALTER TABLE `t_sessioncaisse`
  ADD CONSTRAINT `FK6enmdevjv374hptdgtl5b3yfd` FOREIGN KEY (`organisation_id_organisation_id`) REFERENCES `t_organisation` (`organisation_id`),
  ADD CONSTRAINT `FKon8ieshhijsnjtyj36wp800v2` FOREIGN KEY (`user_utilisateur_id`) REFERENCES `t_utilisateur` (`utilisateur_id`);

--
-- Contraintes pour la table `t_taxeproduit`
--
ALTER TABLE `t_taxeproduit`
  ADD CONSTRAINT `FKhucfbgtio5ps1jfvxol53chkh` FOREIGN KEY (`produit_produit_id`) REFERENCES `t_produit` (`produit_id`),
  ADD CONSTRAINT `FKtm8pkm8sbfctol51qb1kyrx8` FOREIGN KEY (`taxe_taxe_id`) REFERENCES `t_taxe` (`taxe_id`);

--
-- Contraintes pour la table `t_utilisateur`
--
ALTER TABLE `t_utilisateur`
  ADD CONSTRAINT `FKhdm5d2oau3y995sbpwheybhur` FOREIGN KEY (`partenaire_partenaire_id`) REFERENCES `t_partenaire` (`partenaire_id`);

--
-- Contraintes pour la table `t_valeurtest`
--
ALTER TABLE `t_valeurtest`
  ADD CONSTRAINT `FK8yqyj2nu0e780o1or47xhgp9n` FOREIGN KEY (`id_machine`) REFERENCES `t_machine` (`id_machine`),
  ADD CONSTRAINT `FKc8rn65uirgctxynbh1imu30v9` FOREIGN KEY (`id_mesure`) REFERENCES `t_mesure` (`id_mesure`);

--
-- Contraintes pour la table `t_vehicule`
--
ALTER TABLE `t_vehicule`
  ADD CONSTRAINT `FK6pjbufl3y0piedubc4akf5uky` FOREIGN KEY (`marque_vehicule_marque_vehicule_id`) REFERENCES `t_marquevehicule` (`marque_vehicule_id`),
  ADD CONSTRAINT `FKj38ug51jnx9wstsafvaudhdme` FOREIGN KEY (`energie_energie_id`) REFERENCES `t_energie` (`energie_id`);

--
-- Contraintes pour la table `t_vendeur`
--
ALTER TABLE `t_vendeur`
  ADD CONSTRAINT `FKt3t02gp6gxwqtskqm6ehiqwo8` FOREIGN KEY (`partenaire_partenaire_id`) REFERENCES `t_partenaire` (`partenaire_id`);

--
-- Contraintes pour la table `t_vente`
--
ALTER TABLE `t_vente`
  ADD CONSTRAINT `FK2ig0spxlrxgeisti5tuf543g5` FOREIGN KEY (`contact_contact_id`) REFERENCES `t_contact` (`contact_id`),
  ADD CONSTRAINT `FK8r70sh7n7dht5ok2pqnct4r8k` FOREIGN KEY (`visite_id_visite`) REFERENCES `t_visite` (`id_visite`),
  ADD CONSTRAINT `FKg3bsptyyc6el4r9kluycb0ceq` FOREIGN KEY (`client_client_id`) REFERENCES `t_client` (`client_id`),
  ADD CONSTRAINT `FKglemp0qqso8c6v9ch276fvkt7` FOREIGN KEY (`vendeur_vendeur_id`) REFERENCES `t_vendeur` (`vendeur_id`),
  ADD CONSTRAINT `FKpj7yvjg643sjxxn420pwliv1p` FOREIGN KEY (`session_caisse_session_caisse_id`) REFERENCES `t_sessioncaisse` (`session_caisse_id`);

--
-- Contraintes pour la table `t_visite`
--
ALTER TABLE `t_visite`
  ADD CONSTRAINT `FK4l0tfdjj1bfgmpomfiqatvdjv` FOREIGN KEY (`carte_grise_carte_grise_id`) REFERENCES `t_cartegrise` (`carte_grise_id`),
  ADD CONSTRAINT `FKsqur57f1d62twreq225a4fa9a` FOREIGN KEY (`id_caissier`) REFERENCES `t_caissier` (`caissier_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
