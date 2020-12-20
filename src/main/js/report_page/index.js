import React from "react";
import "../styles.css";
import ZapReportApp from "./components/ZapReportApp";

window.registerExtension("zap/report_page", (options) => {
  return
  /* jshint ignore:start */
    <ZapReportApp options={options} />
  /* jshint ignore:end */;
});
