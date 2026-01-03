"use client";
import React, { useState } from "react";
import styles from "./page.module.css";

interface IframeModalProps {
  src: string;
  title?: string;
  buttonLabel?: string;
  generate: () => void;
}

export default function IframeModal({
  src,
  title = "Documento PDF",
  buttonLabel = "Abrir PDF",
  generate,
}: IframeModalProps) {
  const [isOpen, setIsOpen] = useState(false);

  return (
    <div>
      {/* Botón principal */}
      <button
        onClick={() => {
          generate();
          setIsOpen(true);
        }}
        className={styles.generatePdfBtn}
      >
        {buttonLabel}
      </button>

      {isOpen && (
        <div className="fixed inset-0 z-[9999] flex items-center justify-center">
          {/* Overlay oscuro + blur */}
          <div onClick={() => setIsOpen(false)} className="absolute inset-0 bg-black/60 backdrop-blur-md"></div>

          {/* Modal */}
          <div className="relative bg-transparent rounded-lg shadow-2xl w-full max-w-6xl h-[85vh] mx-4 overflow-hidden ">
            {/* Header */}
            <div className="flex justify-end items-center px-6 py-1 h-8 bg-[#003a8f]">
              <button
                onClick={() => setIsOpen(false)}
                className="text-white hover:text-gray-900 font-bold text-xl cursor-pointer"
              >
                ✖
              </button>
            </div>

            {/* Iframe */}
            <div className="w-full h-full">
              <iframe src={src} title={title} className="w-full h-full" style={{ border: "none" }} />
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
