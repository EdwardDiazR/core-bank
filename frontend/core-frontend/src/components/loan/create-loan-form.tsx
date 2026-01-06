import { log } from "console";
import React from "react";

export default function LoanCreationForm() {
  const currencies = [
    {
      label: "DOP - Peso Dominicano",
      value: "DOP",
    },
    {
      label: "USD - Dolar Estadounidense",
      value: "USD",
    },
  ];

  const createLoan = async (formData: FormData) => {

    "use server"
    const createLoanDtoForm = {
      principalAmount: formData.get("amount"),
      interestRate:formData.get("interestRate"),
      termInMonths:formData.get("termInMonths"),
      interestPeriodFrequency:formData.get("interestPeriodFrequency"),
      relatives:[],
      type:formData.get("type"),
      currency:formData.get("currency"),
      signType:formData.get("signType"),
      firstPaymentDate:formData.get("firstPaymentDate")
      //todo: requerir No. de solicitud del credito aprobada
    };

    console.log(formData.get("amount"))

    //TODO: Call service to create loan
  };
  return (
    <div className="h-full bg-slate-50/50 py-1 px-1 overflow-auto">
      <div className="max-w-screen mx-auto">
        {/* Header con Badge de Estado */}
        <div className="flex justify-between items-center mb-5 mt-2 mx-5">
          <h1 className="text-xl text-slate-800 border-l-4 border-[#208b3a] pl-2 mt-2">
            Creacion de <span className="font-semibold">Préstamo</span>
          </h1>
        </div>

        <form className="space-y-6 px-5" action={createLoan}>
          {/* Card: Parámetros Financieros */}
          <div className="bg-white rounded-xl p-8 shadow-[0_8px_30px_rgb(0,0,0,0.04)] border border-slate-100">
            <h2 className="text-sm font-bold text-slate-600 uppercase mb-8 flex items-center gap-2">
              Parametros financieros
            </h2>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-x-12 gap-y-8">
              {/* Amount */}
              <div className="group">
                <label className="block text-[13px] font-medium text-slate-500 mb-2 group-focus-within:text-primaryGreen transition-colors">
                  Monto del Principal
                </label>
                <input
                  name="amount"
                  type="number"
                  placeholder="0.00"
                  className="w-full font-regular bg-transparent border-b-2 border-slate-100 focus:border-primaryGreen outline-none pb-2 transition-all"
                />
              </div>

              {/* Interest Rate */}
              <div className="group">
                <label className="block text-[13px] font-medium text-slate-500 mb-2 group-focus-within:text-green-600">
                  Tasa de Interés Nominal
                </label>
                <div className="flex items-center">
                  <input
                    type="number"
                    step="0.0001"
                    placeholder="0.00"
                    className="w-full text-2xl font-semibold bg-transparent border-b-2 border-slate-100 focus:border-green-600 outline-none pb-2 transition-all"
                  />
                  <span className="text-2xl text-slate-300 ml-2">%</span>
                </div>
              </div>

              {/* Term in Months */}
              <div className="group">
                <label className="block text-[13px] font-medium text-slate-500 mb-2 group-focus-within:text-blue-600 font-mono">
                  termInMonths
                </label>
                <input
                  type="number"
                  className="w-full text-xl bg-transparent border-b-2 border-slate-100 focus:border-blue-600 outline-none pb-2 transition-all"
                />
              </div>

              {/* Currency */}
              <div className="group">
                <label className="block text-[13px] font-medium text-slate-500 mb-2 group-focus-within:text-blue-600">
                  Divisa
                </label>
                <select className="w-full text-xl bg-transparent border-b-2 border-slate-100 focus:border-blue-600 outline-none pb-2 transition-all cursor-pointer">
                  {currencies.map((c, i) => (
                    <option key={i} value={c.value}>
                      {c.label}
                    </option>
                  ))}
                </select>
              </div>
            </div>
          </div>

          {/* Card: Logística de Pagos */}
          <div className="bg-white rounded-3xl p-8 shadow-[0_8px_30px_rgb(0,0,0,0.04)] border border-slate-100">
            <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
              <div className="space-y-2">
                <label className="block text-xs font-bold text-slate-400 uppercase">Frecuencia</label>
                <select className="w-full p-3 bg-slate-50 rounded-xl border-none ring-1 ring-slate-200 focus:ring-2 focus:ring-blue-500 outline-none text-sm font-medium">
                  <option>MENSUAL</option>
                  <option>QUINCENAL</option>
                </select>
              </div>

              <div className="space-y-2">
                <label className="block text-xs font-bold text-slate-400 uppercase">Primer Pago</label>
                <input
                  type="date"
                  className="w-full p-3 bg-slate-50 rounded-xl border-none ring-1 ring-slate-200 focus:ring-2 focus:ring-blue-500 outline-none text-sm"
                />
              </div>

              <div className="space-y-2">
                <label className="block text-xs font-bold text-slate-400 uppercase">Firma</label>
                <select className="w-full p-3 bg-slate-50 rounded-xl border-none ring-1 ring-slate-200 focus:ring-2 focus:ring-blue-500 outline-none text-sm">
                  <option>SOLO_TITULAR</option>
                  <option>CONJUNTA</option>
                </select>
              </div>
            </div>
          </div>

          {/* Sección de Relativos (CreateAccountRelativeDTO) */}
          <div className="bg-blue-600/5 rounded-3xl p-8 border-2 border-dashed border-blue-100">
            <div className="flex justify-between items-center mb-6">
              <div>
                <h3 className="text-blue-900 font-semibold italic">Set&lt;Relatives&gt;</h3>
                <p className="text-blue-600/60 text-xs">Vínculos de cuenta y codeudores</p>
              </div>
              <button
                type="button"
                className="bg-blue-600 text-white px-4 py-2 rounded-full text-xs font-bold hover:shadow-lg hover:shadow-blue-200 transition-all"
              >
                + AGREGAR
              </button>
            </div>

            {/* Item de ejemplo en la lista de familiares */}
            <div className="bg-white p-4 rounded-2xl flex items-center justify-between shadow-sm border border-blue-50">
              <div className="flex items-center gap-4">
                <div className="w-10 h-10 bg-blue-100 rounded-full flex items-center justify-center text-blue-600 font-bold text-xs">
                  JD
                </div>
                <div>
                  <p className="text-sm font-bold text-slate-700">John Doe</p>
                  <p className="text-[10px] text-slate-400 uppercase tracking-tighter">Familiar Directo</p>
                </div>
              </div>
              <button className="text-red-400 hover:text-red-600 text-xs font-medium transition-colors">Remover</button>
            </div>
          </div>

          {/* Footer Actions */}
          <div className="flex items-center justify-between pt-6">
            <button
              type="button"
              className="text-slate-400 text-sm font-semibold hover:text-slate-600 transition-colors"
            >
              Descartar Borrador
            </button>
            <button
              type="submit"
              className="bg-slate-900 text-white px-10 py-4 rounded-2xl font-bold hover:bg-blue-700 transition-all shadow-xl shadow-slate-200 hover:shadow-blue-100"
            >
              Confirmar Apertura
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
