import React from "react";

import { DeferredSpinner } from "sonar-components";
import { getJSON } from "sonar-request";

const findZapReport = (options) => {
  return getJSON("/api/measures/component", {
    component: options.component.key,
    metricKeys: "html_report",
  }).then(function (response) {
    var report = response.component.measures.find((measure) => {
      return measure.metric === "html_report";
    });
    if (typeof report === "undefined") {
      return "<center><h2>No HTML-Report found. Please check property sonar.zaproxy.htmlReportPath</h2></center>";
    } else {
      return report.value;
    }
  });
};

const ZapReportApp = ({ options }) => {
  const [htmlReportString, setHtmlReportString] = React.useState(null);
  const [height, setHeight] = React.useState(
    window.innerHeight - (72 + 48 + 145.5)
  );

  const init = async () => {
    const htmlZapReport = await findZapReport(options);
    setHtmlReportString(htmlZapReport);
  };

  const updateDimensions = () => {
    // 72px SonarQube common pane
    // 72px SonarQube project pane
    // 145,5 SonarQube footer
    let updateHeight = window.innerHeight - (72 + 48 + 145.5);
    setHeight(updateHeight);
  };

  React.useEffect(() => {
    init();
    window.addEventListener("resize", updateDimensions);

    return () => {
      window.removeEventListener("resize", updateDimensions);
    };
  }, []);

  return (
    <div className="page zap-report-container">
      { !htmlReportString &&
        <DeferredSpinner />
      }

      { htmlReportString &&
        <iframe
          classsandbox="allow-scripts allow-same-origin"
          height={height}
          srcDoc={htmlReportString}
          style={{ border: "none" }}
        />
      }
    </div>
  );
};

export default ZapReportApp;
