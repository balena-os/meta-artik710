FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

LINUX_VERSION = "4.4.19"
SRC_URI = " \
    git://git@github.com/SamsungARTIK/linux-artik.git;protocol=ssh;branch=A710/v4.4 \
    file://compile_mali_kernel_module_out_of_tree.patch \
    file://DRM-nexell-Add-support-for-hdmi-1280x1024-60-resolut.patch \
    "
SRCREV = "b3337cbeced5c49415aa405a355d07bb99f6ed43"

inherit kernel
require recipes-kernel/linux/linux-yocto.inc

PV = "${LINUX_VERSION}+git${SRCPV}"

S = "${WORKDIR}/git"

# The defconfig was created with make_savedefconfig so not all the configs are in place
KCONFIG_MODE="--alldefconfig"

COMPATIBLE_MACHINE = "(artik710)"
