DESCRIPTION = "Broadcom Bluetooth patch utility"
SECTION = "connectivity"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRCREV = "6e6506eb74c918e761f9ba08a6c71897334747c1"

SRC_URI = " \
    git://android.googlesource.com/platform/system/bluetooth.git;protocol=https \
    file://add_sleep_to_avoid_hci_reset.patch \
    "
S = "${WORKDIR}/git/brcm_patchram_plus"

do_compile() {
    ${CC} ${LDFLAGS} brcm_patchram_plus.c -o brcm_patchram_plus
}

do_install() {
    install -d  ${D}/${sysconfdir}/bluetooth/
    install -m 0755 ${S}/brcm_patchram_plus ${D}/${sysconfdir}/bluetooth/
}

FILES_${PN}-dbg += "${sysconfdir}/bluetooth/.debug"

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(artik710)"
