<?php
/**
 * The base configurations of the WordPress.
 *
 * This file has the following configurations: MySQL settings, Table Prefix,
 * Secret Keys, WordPress Language, and ABSPATH. You can find more information
 * by visiting {@link http://codex.wordpress.org/Editing_wp-config.php Editing
 * wp-config.php} Codex page. You can get the MySQL settings from your web host.
 *
 * This file is used by the wp-config.php creation script during the
 * installation. You don't have to use the web site, you can just copy this file
 * to "wp-config.php" and fill in the values.
 *
 * @package WordPress
 */

// ** MySQL settings - You can get this info from your web host ** //
/** The name of the database for WordPress */
define('DB_NAME', 'fs2013_phyrex');

/** MySQL database username */
define('DB_USER', 'phyrex');

/** MySQL database password */
define('DB_PASSWORD', 'HPMesrcI');

/** MySQL hostname */
define('DB_HOST', 'webdb.inf.santiago.usm.cl');

/** Database Charset to use in creating database tables. */
define('DB_CHARSET', 'utf8');

/** The Database Collate type. Don't change this if in doubt. */
define('DB_COLLATE', '');

/**#@+
 * Authentication Unique Keys and Salts.
 *
 * Change these to different unique phrases!
 * You can generate these using the {@link https://api.wordpress.org/secret-key/1.1/salt/ WordPress.org secret-key service}
 * You can change these at any point in time to invalidate all existing cookies. This will force all users to have to log in again.
 *
 * @since 2.6.0
 */
define('AUTH_KEY',         '<m6Of;fOCDL.G#kj~Et&BWZuQ:o`b7Y@s*VVr PON5iirzVF&UR=] rD|3eCAB1I');
define('SECURE_AUTH_KEY',  '04->/[Cp QKYdZ&AS{=5:`I*1z>}Z|g5-.7,_GH?yY[5`N+/oBD<p@<pq<mrbf0.');
define('LOGGED_IN_KEY',    '[f_Ql;uB[.,=gN_dlh28S@9]9[.@3qg=30c-;pKkHSDC@J2|k(Z,ZXg,1Ybm-7sG');
define('NONCE_KEY',        'zUHhSz#E531V4C-okOGNclv.(Fbx/3eP;^`P[Vd!qB5dD`ABc@C7gpUCA8QpLuX1');
define('AUTH_SALT',        'O;,kBF9NL_N_K=Vyp7gx7|jY(cl8ef5|^,|P[LsO=>Ob=}S=Xvh1Q+O&3Ul<0u.!');
define('SECURE_AUTH_SALT', 'IgAR?^_S=-4!dorX>8l&UjzM}5~=rm,pTVP^+*EKt~y:L>$lIHZv~;PttbIGW+1]');
define('LOGGED_IN_SALT',   '*~TytEs}+>,|q-Scvx.G U(*4yEf$`n0L:FBNvDA(EL<&-F{exbXw[T?A&ypw$`!');
define('NONCE_SALT',       '~Z(c&[*~%Q:E*#!%`3}zw ;~7>=y|M.RWPkJ3~,S#r1SBvwcx#HM4DGu%,#HEws_');

/**#@-*/

/**
 * WordPress Database Table prefix.
 *
 * You can have multiple installations in one database if you give each a unique
 * prefix. Only numbers, letters, and underscores please!
 */
$table_prefix  = 'wp_';

/**
 * WordPress Localized Language, defaults to English.
 *
 * Change this to localize WordPress. A corresponding MO file for the chosen
 * language must be installed to wp-content/languages. For example, install
 * de_DE.mo to wp-content/languages and set WPLANG to 'de_DE' to enable German
 * language support.
 */
define('WPLANG', '');

/**
 * For developers: WordPress debugging mode.
 *
 * Change this to true to enable the display of notices during development.
 * It is strongly recommended that plugin and theme developers use WP_DEBUG
 * in their development environments.
 */
define('WP_DEBUG', false);

/* That's all, stop editing! Happy blogging. */

/** Absolute path to the WordPress directory. */
if ( !defined('ABSPATH') )
	define('ABSPATH', dirname(__FILE__) . '/');

/** Sets up WordPress vars and included files. */
require_once(ABSPATH . 'wp-settings.php');
