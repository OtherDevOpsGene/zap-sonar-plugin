import React from "react";
import "../styles.css";
import ZapReportApp from "./components/ZapReportApp";

window.registerExtension("zap/report_page", (options) => {
  return <ZapReportApp options={options} />;
});
