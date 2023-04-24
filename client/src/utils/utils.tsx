const validateInput = (value: string, min: number, max: number) => {
    if (value === '') {
        return true;
    }

    const floatRegex = /^-?\d+(\.\d*)?$/;
    if (!floatRegex.test(value)) {
        return false;
    }

    const floatValue = parseFloat(value);
    return floatValue >= min && floatValue <= max;
};

export default validateInput;