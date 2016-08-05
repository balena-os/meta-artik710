DESCRIPTION = "Broadcom 43455c0 WiFi firmware and NVRAM configuration"
SECTION = "connectivity"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://LICENCE.broadcom_bcm43xx;md5=3160c14df7228891b868060e1951dfbc"

SRC_URI = " \
    file://43455c0_apsta.bin \
    file://43455c0_sta.bin \
    file://LICENCE.broadcom_bcm43xx \
    file://nvram.txt \
    "
S = "${WORKDIR}"

do_install() {
        install -d  ${D}/etc/wifi/
        install -m 0755 ${WORKDIR}/43455c0_apsta.bin ${WORKDIR}/43455c0_sta.bin ${WORKDIR}/nvram.txt ${D}/etc/wifi/
        ln -s ./43455c0_sta.bin ${D}/etc/wifi/fw.bin
}

PACKAGE_ARCH = "${MACHINE_ARCH}"

COMPATIBLE_MACHINE = "(artik710)"

#INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
