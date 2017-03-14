FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

LINUX_VERSION = "4.1.15"
SRC_URI = " \
    git://git@github.com/resin-os/kitra710-kernel.git;protocol=ssh \
    file://0002-compiler-gcc6.h-Add-support-for-GCC6.patch \
    file://compile_mali_kernel_module_out_of_tree.patch \
    file://DRM-nexell-Add-support-for-hdmi-1280x1024-60-resolut.patch \
    file://0001-arm64-errata-Add-mpc-relative-literal-loads-to-build.patch \
    "
SRCREV = "15234567a6c5af1be508a3807d1eeb75756c9f51"

inherit kernel
require recipes-kernel/linux/linux-yocto.inc

PV = "${LINUX_VERSION}+git${SRCPV}"

S = "${WORKDIR}/git"

COMPATIBLE_MACHINE = "(kitra710)"
