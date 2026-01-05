import { DivisorLine } from "@/app/shared/DivisorLine";
import { LoanDto } from "@/models/LoanDto";
import { COLORS, FONT_COLORS } from "@/utils/colors";
import { formatNumberWithCurrency } from "@/utils/numberFormatter";
import React from "react";

type Props = {
  loan: LoanDto;
};

export default function DetailPanel({ loan }: Props) {
  const formatDateToLocaleDateString = (date: string) => {
    return new Date(date).toLocaleDateString("es-DO");
  };

  const conditionFields = [
    {
      label: "Tasa de Interés",
      value: `${loan.interestRate}%`,
    },
    {
      label: "Plazo",
      value: `${loan.termInMonths} Meses`,
    },
    {
      label: "Tipo de producto",
      value: loan.type,
    },
    {
      label: "Ultima revision de tasa",
      value: loan.lastInterestRateReviewDate ? formatDateToLocaleDateString(loan.lastInterestRateReviewDate) : "-",
    },
  ];

  const dateFields = [
    {
      label: "Fecha próximo pago",
      value: loan.nextPaymentDate ? formatDateToLocaleDateString(loan.nextPaymentDate) : "-",
    },
    {
      label: "Último Pago realizado",
      value: loan.lastPaymentDate ? formatDateToLocaleDateString(loan.lastPaymentDate) : "-",
    },
    { label: "Fecha de vencimiento", value: loan.dueDate ? formatDateToLocaleDateString(loan.dueDate) : "-" },
  ];

  const otherFields = [
    {
      label: "Monto original",
      value: formatNumberWithCurrency(loan.principalAmount, loan.currency),
    },
    ,
    {
      label: "Linea de credito disponible",
      value: formatNumberWithCurrency(loan.availableAmountForDisbursement, loan.currency),
    },
  ];
  return (
    <div className="w-full bg-gray-100 rounded-md font-sans text-white shadow-md overflow-hidden">
      {/* CABECERA: Azul Institucional con Acento Moderno */}
      <div
        className="px-4 py-2 flex justify-between items-center shadow-md relative"
        style={{ backgroundColor: COLORS.primary }}
      >
        <div>
          <h2 className="text-xl font-bold text-white tracking-tight flex items-center gap-3">
            <span className="text-white text-base font-medium">Prestamo No. {loan.number}</span>
          </h2>
        </div>
        <div className="p-[4px] border-2 rounded-full border-white-600">
          <div
            className={`text-[11px] font-bold px-4 py-1 rounded-full shadow-sm ${
              loan.status === "APPROVED" ? "bg-green-500 text-white" : "bg-red-500 text-white"
            }`}
          >
            {loan.status === "APPROVED" ? "Activo" : "EN MORA"}
          </div>
        </div>
      </div>

      <div className="p-4 space-y-5">
        {/* SEGMENTO 1: BALANCES (Diseño Limpio) */}
        <section>
          <div className="flex items-center gap-2 mb-4">
            <span className="w-1 h-4 rounded-full" style={{ backgroundColor: COLORS.lightGreen }}></span>
            <h3
              className="text-xs font-bold  uppercase tracking-wider"
              style={{
                color: FONT_COLORS.dark,
              }}
            >
              Balances Principales
            </h3>
          </div>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-5">
            <div className="bg-white rounded-xl p-5 shadow-sm relative">
              <label className="text-[11px] font-semibold text-slate-600 uppercase block mb-1">Cuota mensual</label>
              <p className="text-3xl font-semibold text-slate-800 tracking-tight">
                <span className="text-sm font-medium mr-1 text-slate-400">{loan.currency}</span>
                {loan.installmentAmount.toLocaleString("es-DO", { minimumFractionDigits: 2 })}
              </p>
            </div>

            <div className="bg-white rounded-xl p-5 shadow-sm ">
              <label className="text-[11px] font-semibold text-slate-600 uppercase block mb-1">Saldo de Capital</label>
              <p className="text-3xl font-semibold text-slate-800 tracking-tight">
                <span className="text-sm font-medium mr-1 text-slate-400">{loan.currency}</span>
                {loan.outstandingPrincipalBalance.toLocaleString("es-DO", { minimumFractionDigits: 2 })}
              </p>
            </div>

            <div className="bg-white rounded-xl p-5 shadow-sm ">
              <label className="text-[11px] font-semibold text-slate-600 uppercase block mb-1">
                Balance de interes
              </label>
              <p className="text-3xl font-semibold text-slate-800 tracking-tight">
                <span className="text-sm font-medium mr-1 text-slate-400">{loan.currency}</span>
                {loan.interestBalance.toLocaleString("es-DO", { minimumFractionDigits: 2 })}
              </p>
            </div>
          </div>
        </section>
        <DivisorLine />
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          {/* SEGMENTO 2: CRONOLOGÍA (Fondo Cian Muy Claro) */}
          <section className="p-1">
            {/* <div className="flex items-center gap-2 mb-4">
              <h3 className="text-xs font-bold text-[#003a8f] uppercase">Fechas</h3>
            </div> */}
            <div className="space-y-4">
              {dateFields.map((f, i) => (
                <div key={i}>
                  <label className="text-[10px] font-bold text-slate-600 uppercase mb-1 block">{f.label}</label>
                  <input className="w-full bg-white border border-slate-300 px-4 py-2 text-[15px] font-semibold text-slate-900 rounded-lg" disabled value={f.value} />
                </div>
              ))}
            </div>
          </section>

          {/* SEGMENTO 3: CONDICIONES*/}
          <section className="p-1">
            {/* <h3 className="text-xs font-bold text-[#003a8f] mb-5 uppercase tracking-widest flex items-center gap-2">
              Condiciones
            </h3> */}
            <div className="space-y-4">
              {conditionFields.map((f, i) => (
                <div key={i} className="relative">
                  <label className="text-[10px] font-bold text-slate-600 uppercase mb-1 block">{f.label}</label>
                  <input className="w-full bg-white border border-slate-300 px-4 py-2 text-[15px] font-semibold text-slate-900 rounded-lg" disabled value={f.value} />
                    {/* <span className="">{f.value}</span>
                  */}
                  <div className=" absolute top-1 right-3 ">
                    {f.label.toUpperCase() == "TASA DE INTERÉS" && (
                      <div
                        className={`text-[11px] font-bold px-4 py-1 rounded-md shadow-xs  ${
                          loan.status === "APPROVED" ? "bg-sky-500 text-white" : "bg-red-500 text-white"
                        }`}
                      >
                        Anual
                      </div>
                    )}
                  </div>
                </div>
              ))}
            </div>
          </section>

          {/* SEGMENTO 4: PLAZOS*/}
          <section className="p-1">
            {/* <h3 className="text-xs font-bold text-[#003a8f] mb-5 uppercase tracking-widest flex items-center gap-2">
              Gestión
            </h3> */}
            <div className="space-y-4">
              {otherFields.map((f, i) => (
                <div key={i}>
                  <label className="text-[10px] font-bold text-slate-600 uppercase mb-1 block">{f?.label}</label>
                    <input className="w-full bg-white border border-slate-300 px-4 py-2 text-[15px] font-semibold text-slate-900 rounded-lg" disabled value={f?.value} />
                </div>
              ))}
              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="text-[10px] font-bold text-slate-600 uppercase mb-1 block">Cuotas pagadas</label>
                  <div className="bg-white border border-slate-300 px-4 py-2 text-[15px] font-semibold text-slate-900 rounded-lg text-center">
                    {loan.paymentsMade}
                  </div>
                </div>
                <div>
              <label className="text-[10px] font-bold text-slate-600 uppercase mb-1 block">Cuotas pendientes</label>
                  <div className="bg-white border border-slate-300 px-4 py-2 text-[15px] font-semibold text-slate-900 rounded-lg text-center">
                    {loan.paymentsPending}
                  </div>
                </div>
              </div>
            </div>
          </section>
        </div>
      </div>
    </div>
  );
}
