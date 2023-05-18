export const validateInput = (value: string, min: number, max: number) => {
    if (value === "") {
        return true;
    }

    const floatRegex = /^-?\d+(\.\d*)?$/;
    if (!floatRegex.test(value)) {
        return false;
    }

    const floatValue = parseFloat(value);
    return floatValue >= min && floatValue <= max;
};
export const msToHumanReadable = (ms: number): string => {
    const seconds = Math.floor(ms / 1000);
    const minutes = Math.floor(seconds / 60);
    const hours = Math.floor(minutes / 60);
    const days = Math.floor(hours / 24);

    function format(unit: string, value: number) {
        let result = '';
        if (unit === 'dzień') {
            if (value === 1) {
                result = `${value} ${unit}`;
            } else {
                result = `${value} dni`;
            }
        } else if (unit === 'godzin') {
            if (value === 1) {
                result = `${value} godzina`;
            } else if (value % 10 >= 2 && value % 10 <= 4 && !(value % 100 >= 12 && value % 100 <= 14)) {
                result = `${value} godziny`;
            } else {
                result = `${value} godzin`;
            }
        } else if (unit === 'minut') {
            if (value === 1) {
                result = `${value} minuta`;
            } else if (value % 10 >= 2 && value % 10 <= 4 && !(value % 100 >= 12 && value % 100 <= 14)) {
                result = `${value} minuty`;
            } else {
                result = `${value} minut`;
            }
        } else if (unit === 'sekund') {
            if (value === 1) {
                result = `${value} sekunda`;
            } else if (value % 10 >= 2 && value % 10 <= 4 && !(value % 100 >= 12 && value % 100 <= 14)) {
                result = `${value} sekundy`;
            } else {
                result = `${value} sekund`;
            }
        }
        return result;
    }

    if (days > 0) {
        return format('dzień', days);
    } else if (hours > 0) {
        return format('godzin', hours);
    } else if (minutes > 0) {
        return format('minut', minutes);
    } else {
        return format('sekund', seconds);
    }
}
// export default validateInput,msToHumanReadable;