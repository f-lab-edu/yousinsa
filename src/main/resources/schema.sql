SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `sample`;
CREATE TABLE `sample`
(
    `id`      INTEGER AUTO_INCREMENT NOT NULL,
    `comment` TEXT,
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`
(
    `id`            bigint PRIMARY KEY AUTO_INCREMENT,
    `user_name`     varchar(255),
    `user_email`    varchar(255),
    `user_password` varchar(255),
    `user_role`     ENUM ('BUYER', 'STORE_OWNER', 'ADMIN'),
    `created_at`    timestamp,
    `updated_at`    timestamp
);

DROP TABLE IF EXISTS `stores`;
CREATE TABLE `stores`
(
    `id`           bigint PRIMARY KEY AUTO_INCREMENT,
    `store_name`   varchar(255),
    `store_owner`  bigint,
    `store_status` ENUM ('REQUESTED', 'ACCEPTED', 'REJECTED', 'PENDING'),
    `created_at`   timestamp,
    `updated_at`   timestamp
);

DROP TABLE IF EXISTS `products`;
CREATE TABLE `products`
(
    `id`                 bigint PRIMARY KEY AUTO_INCREMENT,
    `store_id`           bigint,
    `sub_category_id`    bigint,
    `category`           ENUM ('TOP', 'OUTER', 'PANTS'),
    `price`              int,
    `product_count`      int,
    `product_size`       varchar(255),
    `thumbnail_image_id` bigint,
    `created_at`         timestamp,
    `updated_at`         timestamp
);

DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`
(
    `id`           bigint PRIMARY KEY AUTO_INCREMENT,
    `buyer_id`     bigint,
    `order_status` varchar(255),
    `created_at`   timestamp,
    `updated_at`   timestamp
);

DROP TABLE IF EXISTS `order_products`;
CREATE TABLE `order_products`
(
    `id`         bigint PRIMARY KEY AUTO_INCREMENT,
    `order_id`   bigint,
    `product_id` bigint,
    `buy_count`  int,
    `created_at` timestamp,
    `updated_at` timestamp
);

DROP TABLE IF EXISTS `reviews`;
CREATE TABLE `reviews`
(
    `id`          bigint PRIMARY KEY AUTO_INCREMENT,
    `product_id`  bigint,
    `reviewer_id` bigint,
    `content`     text,
    `created_at`  timestamp,
    `updated_at`  timestamp
);

DROP TABLE IF EXISTS `events`;
CREATE TABLE `events`
(
    `id`            bigint PRIMARY KEY AUTO_INCREMENT,
    `event_type`    varchar(255),
    `discount_rate` float,
    `started_at`    timestamp,
    `ended_at`      timestamp,
    `created_at`    timestamp,
    `updated_at`    timestamp
);

DROP TABLE IF EXISTS `favorites`;
CREATE TABLE `favorites`
(
    `id`         bigint PRIMARY KEY AUTO_INCREMENT,
    `product_id` bigint,
    `user_id`    bigint,
    `created_at` timestamp
);

DROP TABLE IF EXISTS `product_detail_images`;
CREATE TABLE `product_detail_images`
(
    `id`         bigint PRIMARY KEY AUTO_INCREMENT,
    `product_id` bigint,
    `imagepath`  varchar(255),
    `created_at` timestamp,
    `updated_at` timestamp
);

DROP TABLE IF EXISTS `sub_categories`;
CREATE TABLE `sub_categories`
(
    `id`            bigint PRIMARY KEY AUTO_INCREMENT,
    `category_name` varchar(255),
    `category`      ENUM ('TOP', 'OUTER', 'PANTS'),
    `created_at`    timestamp,
    `updated_at`    timestamp
);

DROP TABLE IF EXISTS `carts`;
CREATE TABLE `carts`
(
    `id`         bigint PRIMARY KEY AUTO_INCREMENT,
    `user_id`    bigint,
    `product_id` bigint,
    `created_at` timestamp,
    `updated_at` timestamp
);

DROP TABLE IF EXISTS `event_products`;
CREATE TABLE `event_products`
(
    `id`         bigint PRIMARY KEY AUTO_INCREMENT,
    `product_id` bigint,
    `event_id`   bigint
);

ALTER TABLE `stores`
    ADD FOREIGN KEY (`store_owner`) REFERENCES `users` (`id`)
        ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `products`
    ADD FOREIGN KEY (`store_id`) REFERENCES `stores` (`id`)
        ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `products`
    ADD FOREIGN KEY (`sub_category_id`) REFERENCES `sub_categories` (`id`)
        ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `products`
    ADD FOREIGN KEY (`thumbnail_image_id`) REFERENCES `product_detail_images` (`id`)
        ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `orders`
    ADD FOREIGN KEY (`buyer_id`) REFERENCES `users` (`id`)
        ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `order_products`
    ADD FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
        ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `order_products`
    ADD FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
        ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `reviews`
    ADD FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
        ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `reviews`
    ADD FOREIGN KEY (`reviewer_id`) REFERENCES `users` (`id`)
        ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `favorites`
    ADD FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
        ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `favorites`
    ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
        ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `product_detail_images`
    ADD FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
        ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `carts`
    ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
        ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `event_products`
    ADD FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
        ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `event_products`
    ADD FOREIGN KEY (`event_id`) REFERENCES `events` (`id`)
        ON DELETE CASCADE ON UPDATE CASCADE;

SET FOREIGN_KEY_CHECKS = 1;
