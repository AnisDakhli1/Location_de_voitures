-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 21, 2025 at 05:08 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `car_rental`
--

-- --------------------------------------------------------

--
-- Table structure for table `cars`
--

CREATE TABLE `cars` (
  `id` int(11) NOT NULL,
  `make` varchar(50) NOT NULL,
  `model` varchar(50) NOT NULL,
  `year` int(11) NOT NULL,
  `license_plate` varchar(20) NOT NULL,
  `color` varchar(30) DEFAULT NULL,
  `daily_rate` decimal(10,2) NOT NULL,
  `available` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `cars`
--

INSERT INTO `cars` (`id`, `make`, `model`, `year`, `license_plate`, `color`, `daily_rate`, `available`) VALUES
(2, 'tesla', 'Model pled', 2015, '258TU1011', 'white', 300.00, 1),
(3, 'BMW', 'M4Cs', 2019, '250TU1234', 'GREN', 1400.00, 0),
(7, 'volkswagen', 'passat', 2019, '168TU990', 'pink', 90.00, 1),
(8, 'toyota', 'HILUX', 2020, '200TU569', 'black', 500.00, 1),
(9, 'KIA', 'RIO', 2016, '170TU896', 'bleu', 250.00, 1),
(10, 'kia', 'sportege', 2024, '220TU9500', 'rouge', 1000.00, 1),
(11, 'Lamborghini', 'Urus', 2025, '260TU8907', 'yellow', 55000.00, 1),
(12, 'Renault', 'R4', 1990, '35TU50', 'blanche', 200.00, 1),
(14, 'kia', 'sportege', 2022, '154TU500', 'red', 600.00, 1);

-- --------------------------------------------------------

--
-- Table structure for table `rentals`
--

CREATE TABLE `rentals` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `car_id` int(11) NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `total_cost` decimal(10,2) NOT NULL,
  `returned` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `rentals`
--

INSERT INTO `rentals` (`id`, `user_id`, `car_id`, `start_date`, `end_date`, `total_cost`, `returned`) VALUES
(4, 3, 2, '2025-05-16', '2025-06-17', 9600.00, 1),
(5, 4, 2, '2025-05-16', '2025-08-17', 27900.00, 1),
(6, 3, 3, '2025-05-16', '2025-06-03', 25200.00, 1),
(9, 1, 7, '2025-04-19', '2025-05-16', 2500.00, 1),
(10, 1, 3, '2025-05-04', '2025-05-07', 5600.00, 0),
(11, 1, 12, '2025-05-16', '2025-05-19', 1500.00, 1),
(12, 1, 11, '2025-05-16', '2025-05-19', 165000.00, 1),
(13, 1, 9, '2025-05-10', '2025-05-14', 1250.00, 1);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `first_name` varchar(100) NOT NULL,
  `last_name` varchar(100) NOT NULL,
  `id_card_number` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `birth_date` date NOT NULL,
  `password` varchar(255) NOT NULL,
  `is_admin` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `first_name`, `last_name`, `id_card_number`, `email`, `birth_date`, `password`, `is_admin`) VALUES
(1, 'anis', 'dakhli', '12345678', 'anis@gamil.com', '2004-04-17', 'anis123456', 0),
(3, 'adbou', 'souayeh', '12345687', 'abdo@gmail.com', '2025-05-16', 'azerty123', 0),
(4, 'ali', 'mdd', '98745632', 'eeeeeeeee@gmail.com', '2025-05-16', '12345678', 0),
(5, 'Yessine', 'Yahyaoui', '12384929', 'yesssineyahoui@gmail.com', '2025-05-16', 'yessine123456', 0),
(7, 'admin', 'admin', '99999999', 'admin@gmail.com', '2000-08-08', 'adminadmin', 1),
(8, 'ranime', 'saadelaoui', '56565656', 'ranim@gmail.com', '2025-05-16', 'ranimeranime', 0),
(9, 'Wiem', 'wiem', '12312312', 'wiem@gmail.xom', '2025-05-16', 'wiemwiem', 0),
(10, 'youssef', 'hafsouni', '12345655', 'youssef@gmail.com', '2025-05-16', '00000000', 0),
(11, 'yassine', 'yahayoui', '30033303', 'yassine@gmail.com', '2004-05-08', '123456789', 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `cars`
--
ALTER TABLE `cars`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `license_plate` (`license_plate`);

--
-- Indexes for table `rentals`
--
ALTER TABLE `rentals`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `car_id` (`car_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id_card_number` (`id_card_number`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `cars`
--
ALTER TABLE `cars`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `rentals`
--
ALTER TABLE `rentals`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `rentals`
--
ALTER TABLE `rentals`
  ADD CONSTRAINT `rentals_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `rentals_ibfk_2` FOREIGN KEY (`car_id`) REFERENCES `cars` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
