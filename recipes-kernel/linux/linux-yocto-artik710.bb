FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

LINUX_VERSION = "4.4.71"
SRC_URI = "git://github.com/SamsungARTIK/linux-artik.git;protocol=https;branch=A710_os_3.1.0"
SRCREV = "ca9c56b32397759e0629b618c5b05155aa31df97"

inherit kernel
require recipes-kernel/linux/linux-yocto.inc

PV = "${LINUX_VERSION}+git${SRCPV}"

S = "${WORKDIR}/git"

# The defconfig was created with make_savedefconfig so not all the configs are in place
KCONFIG_MODE="--alldefconfig"

COMPATIBLE_MACHINE = "(artik710)"
