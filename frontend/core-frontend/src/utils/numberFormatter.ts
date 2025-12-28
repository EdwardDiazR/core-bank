export const formatNumber = (cantidad: number): string => {
  return Intl.NumberFormat("es-DO", {
    style: "currency",
    currencyDisplay: "symbol",
    currency: "DOP",
    minimumFractionDigits: 2,
  }).format(cantidad);
};

export const formatNumberWithCurrency = (cantidad: number, currency: string): string => {
  const locale = currency === "DOP" ? "es-DO" : "es-DO";

  return Intl.NumberFormat('es-DO', {
    style: "currency",
    currencyDisplay: "symbol",
    currency:'DOP',
    minimumFractionDigits: 2,
    currencySign: "standard",
    compactDisplay: "short",
    notation: "standard",
  }).format(cantidad);
};
