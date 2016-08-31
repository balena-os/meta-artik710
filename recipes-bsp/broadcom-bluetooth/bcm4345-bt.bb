DESCRIPTION = "Broadcom Bluetooth fw files and patch utility"
SECTION = "connectivity"

LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://LICENCE.broadcom_bcm43xx;md5=3160c14df7228891b868060e1951dfbc"

SRC_URI = " \
    file://10-local.rules \
    file://BCM4345C0_003.001.025.0111.0194.hcd \
    file://LICENCE.broadcom_bcm43xx \
    file://brcm-bt-firmware.service \
    file://brcm_patchram_plus \
    file://fwdown.sh \
    file://hciconf.sh \
    "
S = "${WORKDIR}"

inherit systemd

SYSTEMD_SERVICE_${PN} = "brcm-bt-firmware.service"

RDEPENDS_${PN} = " \
    bluez5 \
    "

do_install() {
        install -d  ${D}/etc/bluetooth/
        install -m 0755 ${WORKDIR}/BCM4345C0_003.001.025.0108.0000_Generic_UART_37_4MHz_wlbga_ref_iLNA_iTR_eLG_BT40_test_only.hcd ${WORKDIR}/brcm_patchram_plus ${WORKDIR}/fwdown.sh ${WORKDIR}/hciconf.sh ${D}/etc/bluetooth/

        install -d ${D}/etc/udev/rules.d/
        install -m 0755 ${WORKDIR}/10-local.rules ${D}/etc/udev/rules.d/

        if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
            install -d ${D}${systemd_unitdir}/system
            install -c -m 0644 ${WORKDIR}/brcm-bt-firmware.service ${D}${systemd_unitdir}/system
            sed -i -e 's,@BASE_BINDIR@,${base_bindir},g' \
                -e 's,@BASE_SBINDIR@,${base_sbindir},g' \
                -e 's,@SBINDIR@,${sbindir},g' \
                -e 's,@BINDIR@,${bindir},g' \
                -e 's,@SYS_CONFDIR@,${sysconfdir},g' \
                ${D}${systemd_unitdir}/system/brcm-bt-firmware.service
        fi
}

PACKAGE_ARCH = "${MACHINE_ARCH}"

COMPATIBLE_MACHINE = "(artik710)"

INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
