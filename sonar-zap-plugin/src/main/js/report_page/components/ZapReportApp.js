/*
 * ZAP Plugin for SonarQube
 * Copyright (C) 2015-2017 Gene Gotimer
 * gene.gotimer@coveros.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
import React from "react";

import { DeferredSpinner } from "sonar-components";
import { getJSON } from "sonar-request";

const findZapReport = (options) => {
  return getJSON("/api/measures/component", {
    component: options.component.key,
    metricKeys: "html_report",
  }).then(function (response) {
    console.log(response);
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
