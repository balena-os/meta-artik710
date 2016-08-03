require build-artik.inc

DESCRIPTION = "Samsung secure bootloader for Artik 710"
SECTION = "bootloaders"
LICENSE = "GPLv2"

inherit deploy

do_deploy () {
    install -d ${DEPLOYDIR}

    for file in ${LLOADER_BIN} ${BL2_BIN} ${BL31_BIN} ${BL32_BIN} ${NSIH_EMMC} ${NSIH_SD}; do
        install -m 755 ${S}/prebuilt/${MACHINE}/$file ${DEPLOYDIR}
    done

    if [ "${BOOTLOADER_SINGLEIMAGE}" = "1" ]; then
        install ${S}/prebuilt/${MACHINE}/bl1-emmcboot.bin ${S}/prebuilt/${MACHINE}/bl1-sdboot.bin ${DEPLOYDIR}
    else
        install ${S}/prebuilt/${MACHINE}/bl1.bin ${DEPLOYDIR}
    fi
}
addtask deploy before do_build after do_compile

COMPATIBLE_MACHINE = "(artik710)"
PACKAGE_ARCH = "${MACHINE_ARCH}"
