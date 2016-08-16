FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

LINUX_VERSION = "4.1.15"
SRC_URI = " \
    git://github.com/resin-os/linux-artik7.git;protocol=https \
    file://2fa3dc4ea7c1cdf4eed288de9a6cc5d3a8befddd.patch \
    "
SRCREV = "2c84f106d878f9e4f40f6f023f47f1e63e4a0d43"
S = "${WORKDIR}/git"

inherit kernel
require recipes-kernel/linux/linux-yocto.inc

PV = "${LINUX_VERSION}+git${SRCPV}"

# The defconfig was created with make_savedefconfig so not all the configs are in place
KCONFIG_MODE="--alldefconfig"

COMPATIBLE_MACHINE = "(artik710)"

python __anonymous () {
    # FIXME
    # Force 'Image' as make target even if KERNEL_IMAGETYPE is uImage
    # By default it forces zImage
    d.setVar("KERNEL_IMAGETYPE_FOR_MAKE", "Image")
}
