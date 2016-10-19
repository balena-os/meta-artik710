FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

LINUX_VERSION = "4.1.15"
SRC_URI = " \
    git://git@github.com/resin-os/linux-artik7.git;protocol=ssh \
    file://2fa3dc4ea7c1cdf4eed288de9a6cc5d3a8befddd.patch \
    file://compile_mali_kernel_module_out_of_tree.patch \
    "
SRCREV = "fc1a34e44e53a9d063c5d7a618e7fb3b3b01b07d"

inherit kernel
require recipes-kernel/linux/linux-yocto.inc

PV = "${LINUX_VERSION}+git${SRCPV}"

S = "${WORKDIR}/git"

# The defconfig was created with make_savedefconfig so not all the configs are in place
KCONFIG_MODE="--alldefconfig"

COMPATIBLE_MACHINE = "(artik710)"
